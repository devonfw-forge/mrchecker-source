package com.capgemini.mrchecker.mobile;

import org.junit.Test;

import com.capgemini.mrchecker.mobile.core.BasePage;

public class BasePageTest {



	//TODO https://www.guru99.com/introduction-to-appium.html

	@Test
	public void test() {
		MyPage myPage = new MyPage();
		
		myPage.myMethod();
	}
	
	private static class MyPage extends BasePage {
		
		public String myMethod() {
			return "Welcome";
		}
	}
	
}
