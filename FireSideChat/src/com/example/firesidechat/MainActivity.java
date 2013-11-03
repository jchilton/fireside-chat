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
	
	private boolean isLoggedIn = false;
	

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
            	
            	
                Intent intent = new Intent(MainActivity.this, chat_activity.class);
                startActivity(intent);
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
		try {
			JSONObject jsonobj = new JSONObject(result);
		} catch (JSONException jse) {
			Log.d("Result error", "yeah");
			
		}
	}
}
