package com.example.firesidechat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firesidechat.model.Message;
import com.example.firesidechat.model.MessageList;
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
	
	MessageList messages;
	
	Timer messagesTimer = new Timer();
	final Long TIMER_DELAY = (long) 20 * 1000;
	String lastTimeStamp = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        
        messages = new MessageList();
        
    	String time = new Timestamp(System.currentTimeMillis()).toString();
        lastTimeStamp = time.substring(0, time.lastIndexOf('.'));
        
        //get intent from previous activity
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        topicName = intent.getStringExtra("topicName");
        topicId = intent.getIntExtra("topicId", 0);

        TextView tag = (TextView) findViewById(R.id.chat_tags);
        tag.setText("Topic: " + topicName);
        
        messagesTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateMessages();
			}
		}, 0, TIMER_DELAY);

        final TextView msg_data = (TextView) findViewById(R.id.message_text_field);
        Button msg_btn = (Button) findViewById(R.id.message_send_button);
        msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://stackoverflow.com/questions/6369062/how-do-i-add-elements-dynamically-to-a-view-created-with-xml


            }
        });
        
        
    }
    public void displayMessage(String msg_data, String username, String timestamp) { 
    	//will post message
    	//timesteamp
    	//then call pull message with boolean overRideTimer=True
    }

    
    

	public void updateMessages() {
        HashMap<String, String> requestVals = new HashMap<String, String>();
        requestVals.put("username", username);
        requestVals.put("password", password);
        
        Log.d("TimeStamp", "asking for msgs later than " + lastTimeStamp);
        requestVals.put("timestamp", lastTimeStamp);
    	
    	JSONObject obj = new JSONObject(requestVals);
        try {
        	obj.put("topic_id", Integer.valueOf(topicId));
        	new SearchMessagesRequestTask(this).execute(Server.MESSAGES_URL, obj.toString());
        }
        catch (JSONException e) { }
        
    	String time = new Timestamp(System.currentTimeMillis()).toString();
        lastTimeStamp = time.substring(0, time.lastIndexOf('.'));
        
        for(Message m : messages.getMessages()) {
        	Log.d("Messages", m.toString()+" ");
        }
	}

	public void addMessages(String response) {
		Log.d("Response", response);
		JSONObject json = null;
		try {
			JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				
				messages.add(new Message(jsonObj.getString("username"),
										 jsonObj.getString("timestamp"),
										 jsonObj.getString("message")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onPostReturn(String URL, String response) {
		if (URL.equals(Server.MESSAGES_URL)) {
			addMessages(response);
		}
		else if (URL.equals("")) {
			
		}
	}

}
