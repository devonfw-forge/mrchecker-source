package com.capgemini.mrchecker.mobile.unit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.mobile.core.BasePage;
import com.capgemini.mrchecker.mobile.tags.UnitTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.PageFactory;

@UnitTest
@ResourceLock("PropertiesFileSettings.class")
@Disabled("Due to no appium running automatically")
public class BasePageTest {
	
	@Test
	public void test() {
		MyPage myPage = PageFactory.getPageInstance(MyPage.class);
		myPage.myMethod();
	}
	
	public class MyPage extends BasePage {
		
		public String myMethod() {
			BFLogger.logDebug("TITLE: " + BasePage.getDriver()
					.getPageSource());
			
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
