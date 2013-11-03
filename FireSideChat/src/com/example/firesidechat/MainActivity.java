package com.example.firesidechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.firesidechat.web.HasHttpPostCallback;
import com.example.firesidechat.web.HttpPostTask;
import com.example.firesidechat.web.LoginRequestTask;
import com.example.firesidechat.web.SearchTopicRequestTask;
import com.example.firesidechat.web.Server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements HasHttpPostCallback {

	private void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button init_chat = (Button) findViewById(R.id.chat_init_button);
		final MainActivity m = this;
		init_chat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("username",
						((TextView) findViewById(R.id.username_field))
								.getText().toString());
				params.put("password",
						((TextView) findViewById(R.id.password_field))
								.getText().toString());
				params.put("topic", ((TextView) findViewById(R.id.tag_field))
						.getText().toString());
				params.put("oneonone", "N");
				new LoginRequestTask(m).execute(Server.JOIN_URL, new JSONObject(
						params).toString());
			}
		});

		AutoCompleteTextView tag_field = (AutoCompleteTextView) findViewById(R.id.tag_field);
		tag_field.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("prefix", ((TextView) findViewById(R.id.tag_field))
						.getText().toString());
				new SearchTopicRequestTask(m).execute(Server.SEARCH_URL, new JSONObject(
						params).toString());				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void loginRequestCompleted(String result) {
		Log.d("Result", result);
		JSONObject obj = null;
		try {
			obj = new JSONObject(result);
		} catch (JSONException jse) {
			Log.d("Result error", "yeah");
			showMessage("Server error; please contact the developer");
			return;
		}
		try {
			Integer errorCode = (Integer) obj.get("error");
			if (Server.RESPONSE_CODES.containsKey(errorCode)) {
				showMessage(Server.RESPONSE_CODES.get(errorCode));
				return;
			} else {
				Log.d("Result", String.valueOf(errorCode));
				showMessage("The server reported an unknown error.");
				return;
			}
		} catch (JSONException e) {
			// There was no error on the object. Hooray.
		}

		try {
			obj.get("success"); // Throw if this isn't there
			Integer topicId = (Integer) obj.get("topic_id");
			switchToChat(topicId);
		} catch (JSONException e) {
			showMessage("The server responded with an unknown status.");
			return;
		}
	}

	public void switchToChat(Integer topicId) {
		Intent intent = new Intent(MainActivity.this, ChatActivity.class);
		intent.putExtra("topicID", topicId);
		startActivity(intent);
	}

	public void searchRequestCompleted(String response) {
		Log.d("Result", response);
		JSONObject obj = null;
		try {
			obj = new JSONObject(response);
		} catch (JSONException jse) {
			Log.d("Result error", "yeah");
			showMessage("Server error; please contact the developer");
			return;
		}
		try {
			Integer errorCode = (Integer) obj.get("error");
			if (Server.RESPONSE_CODES.containsKey(errorCode)) {
				showMessage(Server.RESPONSE_CODES.get(errorCode));
				return;
			} else {
				Log.d("Result", String.valueOf(errorCode));
				showMessage("The server reported an unknown error.");
				return;
			}
		} catch (JSONException e) {
			// There was no error on the object. Hooray.
		}

		try {
			JSONArray topics = obj.getJSONArray("data"); // Throw if this isn't there
			List<String> topics_ = new ArrayList<String>();
			for (int i = 0; i < topics.length(); i++) {
				topics_.add((String)topics.get(i));
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, topics_);
			((AutoCompleteTextView) findViewById(R.id.tag_field)).setAdapter(adapter);
		} catch (JSONException e) {
			showMessage("The server responded with an unknown status.");
			return;
		}
		catch (Exception e) {
			// fail silently here
		}
	}

	@Override
	public void onPostReturn(String URL, String response) {
		if (URL.equals(Server.JOIN_URL)) {
			loginRequestCompleted(response);
		}
		else if (URL.equals(Server.SEARCH_URL)) {
			searchRequestCompleted(response);
		}
	}
}
