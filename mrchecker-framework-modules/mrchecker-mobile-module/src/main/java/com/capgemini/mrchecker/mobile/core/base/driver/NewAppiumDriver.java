package com.capgemini.mrchecker.mobile.core.base.driver;

import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;

public final class NewAppiumDriver extends AppiumDriver implements INewMobileDriver {
	
	public NewAppiumDriver(URL url, DesiredCapabilities capabilities) {
		super(url, capabilities);
	}
	
}