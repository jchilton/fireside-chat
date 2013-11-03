package com.example.firesidechat;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.firesidechat.model.Message;
import com.example.firesidechat.model.MessageList;
import com.example.firesidechat.web.HasHttpPostCallback;
import com.example.firesidechat.web.SearchMessagesRequestTask;
import com.example.firesidechat.web.Server;
//import com.example.firesidechat.web.SearchMessagesRequestTask;
import com.example.firesidechat.web.SubmitMessageRequestTask;

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
        
		//sets keyboard to start hidden until soft input selected

        
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
            	//on click send HTTPPostTask
            	submitMessage(msg_data.getText().toString());
            	
            	displayUpdate();
            	
            	msg_data.setText("");
            	

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
			displayUpdate();
		}
		else if (URL.equals(Server.SUBMIT_URL)) {
			
		}
	}

	public void submitMessage(String msg_data) {
    	String[] sa = new String[2];
    	sa[0] = Server.SUBMIT_URL; 
    	HashMap<String, String> submitVals = new HashMap<String, String>();
    	submitVals.put("username", username);
    	submitVals.put("password", password);
    	submitVals.put("topic_id", Integer.toString(topicId));
    	submitVals.put("message", msg_data);
    	sa[1] = new JSONObject(submitVals).toString();
    	
    	new SubmitMessageRequestTask(this).execute(sa);
    
	}
	
	public void displayUpdate() { 
		for (Message msg: messages.getMessages()) { 
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View v = inflater.inflate(R.layout.chat_activity, null);

		    // Find the ScrollView 
		    ScrollView sv = (ScrollView) v.findViewById(R.id.chat_scroll_view);

		    // Create a LinearLayout element
		    LinearLayout ll = new LinearLayout(this);
		    ll.setOrientation(LinearLayout.VERTICAL);
		    ll.setPadding(15, 15, 15, 15);

		    // Add text
		    TextView name_msg1 = new TextView(this);
		    name_msg1.setText(msg.getUsername() + ":  "+ msg.getMessage());
		    ll.addView(name_msg1);
		    
		    TextView ts = new TextView(this);
		    ts.setText(msg.getTimestamp().toString());
		    ll.addView(ts);
		    
		   
		    // Add the LinearLayout element to the ScrollView
		    sv.addView(ll);

		    // Display the view
		    setContentView(v);
		}

	}
}
