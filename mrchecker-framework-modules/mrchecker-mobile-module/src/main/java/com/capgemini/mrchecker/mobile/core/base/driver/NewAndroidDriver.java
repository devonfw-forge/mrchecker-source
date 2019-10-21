package com.capgemini.mrchecker.mobile.core.base.driver;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.*;
import com.capgemini.mrchecker.test.core.BaseTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public final class NewAndroidDriver extends AndroidDriver implements INewMobileDriver {

	private DriverExtention driverExtention;

	public NewAndroidDriver(ChromeOptions options) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Android Emulator");
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("appPackage", "io.appium.android.apis");
		capabilities.setCapability("appActivity", ".ApiDemos");
		super(getServiceUrl(), capabilities);
	}

	public static void main(String[] args) {
		BaseTest.getAnalytics()
				.sendMethodEvent(BasePage.analitycsCategoryName);
	}

}