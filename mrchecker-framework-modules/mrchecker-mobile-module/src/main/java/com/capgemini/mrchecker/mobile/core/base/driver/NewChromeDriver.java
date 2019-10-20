package com.capgemini.mrchecker.mobile.core.base.driver;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.*;
import com.capgemini.mrchecker.test.core.BaseTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class NewChromeDriver extends AppiumDriver implements INewMobileDriver {

	private DriverExtention driverExtention;


	public NewChromeDriver(ChromeOptions options) {
		super(options);
	}

	public static void main(String[] args) {
		BaseTest.getAnalytics()
				.sendMethodEvent(BasePage.analitycsCategoryName);
	}

}