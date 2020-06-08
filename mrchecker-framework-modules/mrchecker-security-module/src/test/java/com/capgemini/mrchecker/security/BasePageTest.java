package com.capgemini.mrchecker.security;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.security.core.BasePage;
import com.capgemini.mrchecker.test.core.utils.PageFactory;

public class BasePageTest {
	
	@Test
	public void test() {
		MyPage myPage = PageFactory.getPageInstance(MyPage.class);
		myPage.myMethod();
	}
	
	public static class MyPage extends BasePage {
		
		public String myMethod() {
			return "Welcome";
		}
	}
	
}
