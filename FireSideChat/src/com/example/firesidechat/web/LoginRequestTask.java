package com.example.firesidechat.web;

public class LoginRequestTask extends HttpPostTask {
	public LoginRequestTask(HasHttpPostCallback a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPostExecute(String result) {
		callbackParent.onPostReturn(Server.JOIN_URL, result);
	}
}
