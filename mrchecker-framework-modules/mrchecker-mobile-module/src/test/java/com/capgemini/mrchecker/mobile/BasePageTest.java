package com.capgemini.mrchecker.mobile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.mobile.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;

@ResourceLock("PropertiesFileSettings.class")
public class BasePageTest {
	
	@Test
	public void test() {
		MyPage myPage = new MyPage();
		myPage.initialize();
		myPage.myMethod();
	}
	
	public class MyPage extends BasePage {
		
		public String myMethod() {
			BFLogger.logDebug("TITLE: " + getDriver().getPageSource());
			
			// File file = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
			// try {
			// FileUtils.copyFile(file, new File("Screenshot.jpg"));
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			
			return "Welcome";
		}
	}
	
}
