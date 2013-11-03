package com.example.firesidechat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firesidechat.web.HasHttpPostCallback;
import com.example.firesidechat.web.SearchMessagesRequestTask;
import com.example.firesidechat.web.Server;

/**
 * Created by timlarson on 11/2/13.
 */
public class ChatActivity extends Activity implements HasHttpPostCallback{
	String username;
	String password;
	String topicName;
	Integer topicId;
	ArrayList<JSONObject> messages;
	Timer timer = new Timer();
	final Long timer_delay = (long) 10000;
	final ChatActivity ca = this;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        
        //get intent from previous activity
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        topicName = intent.getStringExtra("topicName");
        topicId = intent.getIntExtra("topicId", 0);

        TextView tag = (TextView) findViewById(R.id.chat_tags);
        tag.setText("Topic: " + topicName);
        
        timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateMessages();
			}
		}, 0, timer_delay);

        final TextView msg_data = (TextView) findViewById(R.id.message_text_field);
        Button msg_btn = (Button) findViewById(R.id.message_send_button);
        msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://stackoverflow.com/questions/6369062/how-do-i-add-elements-dynamically-to-a-view-created-with-xml


            }
        });
    }

	public void updateMessages() {
        HashMap<String, String> requestVals = new HashMap<String, String>();
        requestVals.put("username", username);
        requestVals.put("password", password);
        requestVals.put("topic_id", Integer.toString(topicId));
        requestVals.put("timestame", new Timestamp(System.currentTimeMillis()).toString());
		new SearchMessagesRequestTask(ca).execute(Server.MESSAGES_URL, new JSONObject(requestVals).toString());
	}

	@Override
	public void onPostReturn(String URL, String response) {
		Log.d("Response", response);
		JSONObject json = null;
		try {
			json = new JSONObject(response);
			messages = new ArrayList<JSONObject>();
			json.getJSONArray("data");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
