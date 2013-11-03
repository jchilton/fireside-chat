package com.example.firesidechat;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {	

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
        		params.put("username", ((TextView)findViewById(R.id.username_field)).getText().toString());
        		params.put("password", ((TextView)findViewById(R.id.password_field)).getText().toString());
        		params.put("topic", ((TextView)findViewById(R.id.tag_field)).getText().toString());
        		params.put("oneonone", "N");
        		new LoginRequestTask(m).execute(Server.JOIN_URL, new JSONObject(params).toString());
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
			Integer errorCode = (Integer)obj.get("error");
			if (Server.RESPONSE_CODES.containsKey(errorCode)) {
				showMessage(Server.RESPONSE_CODES.get(errorCode));
				return;
			} else {
				Log.d("Result", String.valueOf(errorCode));
				showMessage("The server reported an unknown error.");
				return;
			}
		}
		catch (JSONException e) {
			//There was no error on the object. Hooray.
		}
		
		try {
			obj.get("success"); // Throw if this isn't there
			Integer topicId = (Integer)obj.get("topic_id");
			switchToChat(topicId);
		}
		catch (JSONException e) {
			showMessage("The server responded with an unknown status.");
			return;
		}
	}
	
	public void switchToChat(Integer topicId) {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra("topicID", topicId);
        startActivity(intent);
	}
}
