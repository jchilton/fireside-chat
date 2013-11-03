package com.example.firesidechat;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by timlarson on 11/2/13.
 */
public class chat_activity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        //http://stackoverflow.com/questions/6369062/how-do-i-add-elements-dynamically-to-a-view-created-with-xml

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.chat_activity, null);

        ScrollView sv = (ScrollView) v.findViewById(R.id.chat_scroll_view);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);



        TextView tv = new TextView(this);
        tv.setText("Usernname: ");
        ll.addView(tv);

        TextView message = new TextView(this);
        message.setText("Message here");
        ll.addView(message);

        sv.addView(ll);

        setContentView(v);

    }
}