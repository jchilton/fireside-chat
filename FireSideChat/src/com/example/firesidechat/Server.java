package com.example.firesidechat;

import java.util.HashMap;

import org.json.JSONObject;

public class Server {
	private static String url = "http://10.20.11.49/join";
	private static MainActivity parent;
	
	public static void setMainActivity(MainActivity activity) {
		parent = activity;
	}

	public static void login(String username, String password, String topic, MainActivity activity) {
		setMainActivity(activity);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		params.put("topic", topic);
		params.put("oneonone", "n");
		JSONObject jsonObject;
		new LoginRequestTask(parent).execute(url, new JSONObject(params).toString());
	}
}
