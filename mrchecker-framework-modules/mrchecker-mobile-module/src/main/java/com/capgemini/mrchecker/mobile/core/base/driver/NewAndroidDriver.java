package com.capgemini.mrchecker.mobile.core.base.driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;

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
		// if it needs to use locally started server
		// then the target_ip is 127.0.0.1 or 0.0.0.0
		// the default port is 4723
		
		return url;
	}
}