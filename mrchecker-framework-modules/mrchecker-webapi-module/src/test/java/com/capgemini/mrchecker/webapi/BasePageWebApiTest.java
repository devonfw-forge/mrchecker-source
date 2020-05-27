package com.capgemini.mrchecker.webapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.webapi.core.BasePageWebAPI;

@ResourceLock("PropertiesFileSettings.class")
public class BasePageWebApiTest extends BaseTest {
	
	@Override
	public void setUp() {
		
	}
	
	@Override
	public void tearDown() {
		
	}
	
	@Test
	public void test() {
		MyPage myPage = new MyPage();
		myPage.initialize();
		myPage.myMethod();
	}
	
	private static class MyPage extends BasePageWebAPI {
		
		public String myMethod() {
			return "Welcome";
		}
		
		@Override
		public String getEndpoint() {
			return null;
		}
	}
	
}
