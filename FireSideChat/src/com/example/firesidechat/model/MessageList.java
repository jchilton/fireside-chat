package com.example.firesidechat.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MessageList {
	ArrayList<Message> messages;
	HashMap<Integer, Boolean> keys;
	
	public MessageList() {
		messages = new ArrayList<Message>();
		keys = new HashMap<Integer, Boolean>();
	}
	
	public void add(Message m) {
		StringBuilder sb = new StringBuilder();
		sb.append(m.getUsername());
		sb.append(m.getTimestamp());
		sb.append(m.getMessage());
		
		Integer hash = sb.toString().hashCode();
		if (keys.containsKey(hash)) {
			return;
		}
		
		messages.add(m);
		keys.put(hash, true);
		
		Collections.sort(messages);
	}
	
	public ArrayList<Message> getMessages() {
		return messages;
	}

}
