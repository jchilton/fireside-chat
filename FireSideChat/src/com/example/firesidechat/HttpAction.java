package com.example.firesidechat;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAction extends AsyncTask<String, Void, String> {
	
	public HttpAction() {
		
	}
	
	public void POST(String url, HashMap<String, String> params) {
		HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        InputStream in = null;

        try {
            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity(writeJSON(params));  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if(response!=null){
                in = response.getEntity().getContent(); //Get the data in the entity
                Log.d("response", in.toString());
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

	}

	private String writeJSON(HashMap<String, String> params) {
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
		  return object.toString();
		}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	} 
}
