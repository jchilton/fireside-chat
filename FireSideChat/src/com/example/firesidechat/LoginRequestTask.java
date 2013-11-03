package com.example.firesidechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;

public class LoginRequestTask extends AsyncTask<String, Void, String> {

	private MainActivity callbackParent;
	
	public LoginRequestTask(MainActivity a) {
		this.callbackParent = a;
	}

	@Override
	/**
	 * Contract: The first string is the URL to connect to, the second
	 * 				is the request JSON
	 * 				
	 */
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		StringBuilder builder = new StringBuilder();
		try {
			HttpPost request = new HttpPost(params[0]);
			request.addHeader("content-type", "application/json");
			request.setEntity(new StringEntity(params[1]));
			HttpResponse response = httpClient.execute(request);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				System.out.println("Failed to download");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	} 
	
	@Override
	public void onPostExecute(String result) {
		callbackParent.loginRequestCompleted(result);
	}
}
