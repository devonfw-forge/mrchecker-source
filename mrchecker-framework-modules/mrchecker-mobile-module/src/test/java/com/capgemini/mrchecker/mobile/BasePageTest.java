package com.capgemini.mrchecker.mobile;

import com.capgemini.mrchecker.mobile.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class BasePageTest {

	@Test
	public void test() {
		MyPage myPage = new MyPage();
		myPage.myMethod();
	}

	public class MyPage extends BasePage {

		public String myMethod() {
			BFLogger.logDebug("TITLE: " + getDriver().getPageSource());
			BFLogger.logDebug("AppStringMap: " + getDriver().getAppStringMap());

			File file  = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(file, new File("Screenshot.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			//			getDriver().getTitle();
//			getDriver().getCurrentUrl();
			return "Welcome";
		}
	}



}


