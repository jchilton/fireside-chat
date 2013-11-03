package com.example.firesidechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by timlarson on 11/2/13.
 */
public class ChatActivity extends Activity {
	String username;
	String password;
	String topicName;
	Integer topicId;
	
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

    
    


}
