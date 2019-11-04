package com.capgemini.mrchecker.mobile.core.base.driver;

import com.capgemini.mrchecker.mobile.core.BasePage;
//import com.capgemini.mrchecker.selenium.core.BasePage;
//import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
//import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.*;
import com.capgemini.mrchecker.test.core.BaseTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public final class NewAndroidDriver extends AndroidDriver implements INewMobileDriver {

	public NewAndroidDriver(DesiredCapabilities capabilities) {
		super(getServiceUrl(), capabilities);

	}

	private static URL getServiceUrl() {
		URL url = null;
		try {
			url = new URL("http://target_ip:used_port/wd/hub");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//if it needs to use locally started server
				//then the target_ip is 127.0.0.1 or 0.0.0.0
				//the default port is 4723

		return url;
	}

	public static void main(String[] args) {
		BaseTest.getAnalytics()
				.sendMethodEvent(BasePage.analitycsCategoryName);
	}

}