package com.example.firesidechat.web;

import com.example.firesidechat.MainActivity;

public class SearchTopicRequestTask extends HttpPostTask {
	public SearchTopicRequestTask(MainActivity a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPostExecute(String result) {
		callbackParent.searchRequestCompleted(result);
	}
}
