package com.example.firesidechat.web;

public class SubmitMessageRequestTask extends HttpPostTask {

	public SubmitMessageRequestTask(HasHttpPostCallback a) {
		super(a);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public void onPostExecute(String result) {
		callbackParent.onPostReturn(Server.SUBMIT_URL, result);
	}
	
}
