package com.capgemini.mrchecker.test.core.utils;

import io.qameta.allure.Attachment;

public class Attachments {
	private Attachments() {
		
	}
	
	@Attachment("Log file")
	public static String attachToAllure(String content) {
		return content;
	}
}
