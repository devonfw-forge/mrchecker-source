package com.capgemini.mrchecker.mobile.core.base.driver;

import com.capgemini.mrchecker.mobile.core.BasePage;
import com.capgemini.mrchecker.test.core.BaseTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;


public final class NewAppiumDriver extends AppiumDriver implements INewMobileDriver {

	public NewAppiumDriver(URL url, DesiredCapabilities capabilities) {
		super(url , capabilities);

	}

	public static void main(String[] args) {
		BaseTest.getAnalytics()
				.sendMethodEvent(BasePage.analitycsCategoryName);
	}

}