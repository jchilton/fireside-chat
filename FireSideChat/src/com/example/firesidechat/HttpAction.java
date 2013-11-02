package com.example.firesidechat;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class HttpAction extends AsyncTask<String, Void, String> {
	
	public HttpAction() {
		
	}

	private void writeJSON(HashMap<String, String> params) {
		  JSONObject object = new JSONObject();
		  try {
			  for (String key : params.keySet()) 
				  object.put(key, params.get(key));
//		    object.put("name", "Jack Hack");
//		    object.put("score", new Integer(200));
//		    object.put("current", new Double(152.32));
//		    object.put("nickname", "Hacker");
		  } catch (JSONException e) {
		    e.printStackTrace();
		  }
		  System.out.println(object);
		}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	} 
}
