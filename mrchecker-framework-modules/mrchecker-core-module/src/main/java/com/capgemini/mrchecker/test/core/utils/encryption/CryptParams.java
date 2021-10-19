package com.capgemini.mrchecker.test.core.utils.encryption;

public class CryptParams {
	private String secret;
	private String text;
	
	public CryptParams(String secret, String text) {
		this.secret = secret;
		this.text = text;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public String getText() {
		return text;
	}
}
