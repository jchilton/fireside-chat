package com.example.firesidechat.web;

public class SearchTopicRequestTask extends HttpPostTask {
	public SearchTopicRequestTask(HasHttpPostCallback a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPostExecute(String result) {
		callbackParent.onPostReturn(Server.SEARCH_URL, result);
	}
}
