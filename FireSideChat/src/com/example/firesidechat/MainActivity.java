package com.example.firesidechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText responseField;
	TextView isConnectedField;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		isConnectedField = (EditText) findViewById(R.id.username_field);
		responseField = (EditText) findViewById(R.id.tag_field);

		if(isConnected()) {
			isConnectedField.setText("Connected");
			Log.d("Network Connectivity", "Connected");
		} else {
			isConnectedField.setText("NOT Connected");
			Log.d("Network Connectivity", "NOT Connected");
		}
		
		new HttpAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	public static String GET(String url) {
		InputStream istream = null;
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(url));
			istream = response.getEntity().getContent();
			if (istream != null)
				result = convertInputStreamToString(istream);
			else
				result = "Did not work!";
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		
		inputStream.close();
		return result;
	}
	
	public boolean isConnected() {
		ConnectivityManager cmanager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = cmanager.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isConnected())
			return true;
		else return false;
	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String ... urls) {
			return GET(urls[0]);
		}
		
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Recieved!", Toast.LENGTH_LONG).show();	
			responseField.setText(result);
		}
	}
}
