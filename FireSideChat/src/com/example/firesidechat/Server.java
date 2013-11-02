package com.example.firesidechat;

import java.util.HashMap;

public class Server {
	private static String url = "http://10.20.11.49/join";

	public static void login(String username, String password, String topic) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		params.put("topic", topic);
		params.put("oneonone", "n");
		new HttpAction().POST(url, params);
	}
}
