package com.capgemini.mrchecker.mobile.core.base.driver;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public final class NewAppiumDriver extends AppiumDriver implements INewMobileDriver {

    public NewAppiumDriver(URL url, DesiredCapabilities capabilities) {
        super(url, capabilities);
    }

}