package com.example.firesidechat.web;

import com.example.firesidechat.MainActivity;

public class LoginRequestTask extends HttpPostTask {
	public LoginRequestTask(MainActivity a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPostExecute(String result) {
		callbackParent.loginRequestCompleted(result);
	}
}
