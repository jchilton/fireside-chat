package com.example.firesidechat.web;

public class SearchMessagesRequestTask extends HttpPostTask {

	public SearchMessagesRequestTask(HasHttpPostCallback a) {
		super(a);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void onPostExecute(String result) {
		callbackParent.onPostReturn(Server.MESSAGES_URL, result);
	}
}
