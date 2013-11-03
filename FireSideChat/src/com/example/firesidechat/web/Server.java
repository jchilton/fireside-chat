package com.example.firesidechat.web;

import java.util.HashMap;

import org.json.JSONObject;

public class Server {
	public static String JOIN_URL = "http://10.20.11.49/join";
	public static String SEARCH_URL = "http://10.20.11.49/search_topics";
	public static String MESSAGES_URL = "http://10.20.11.49/get_messages";
	
	public static HashMap<Integer, String> RESPONSE_CODES = new HashMap<Integer, String>();
	static {
		RESPONSE_CODES.put(1, "There was a database error. Please contact the developer.");
		RESPONSE_CODES.put(3, "Your username and password combination did not authenticate. Please check your spelling.");
		RESPONSE_CODES.put(5, "Your password needs to be at least 8 characters.");
		RESPONSE_CODES.put(12, "Your username needs to be at least 3 characters (no whitespace).");
	}
}
