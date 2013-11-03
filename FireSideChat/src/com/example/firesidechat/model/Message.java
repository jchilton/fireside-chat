package com.example.firesidechat.model;

import java.sql.Timestamp;

public class Message implements Comparable<Message> {
	String username;
	Timestamp timestamp;
	String message;
	
	public Message(String username, String timestamp, String message) {
		this.username = username;
		this.timestamp = Timestamp.valueOf(timestamp);
		this.message = message;
	}
	
	public String getUsername() {
		return username;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public String getMessage() {
		return message;
	}

	@Override
	public int compareTo(Message other) {
		return this.timestamp.getNanos() - other.getTimestamp().getNanos();
	}
	
	public String toString() {
		return "("+getMessage()+", "+getUsername()+", "+getTimestamp().toString()+")";
	}
}
