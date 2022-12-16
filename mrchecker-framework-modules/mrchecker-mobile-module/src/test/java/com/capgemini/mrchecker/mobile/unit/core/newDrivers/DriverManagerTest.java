package com.capgemini.mrchecker.mobile.unit.core.newDrivers;

import com.capgemini.mrchecker.mobile.core.base.driver.DriverManager;
import com.capgemini.mrchecker.mobile.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.mobile.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.mobile.tags.UnitTest;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.google.inject.Guice;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

@UnitTest
@ResourceLock("PropertiesFileSettings.class")
@Disabled("Due to no appium running automatically")
public class DriverManagerTest {

    private DriverManager driverManager;

    @BeforeEach
    public void setUp() {

        String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/Simple_App_v2.0.1_apkpure.com.apk");
        File app = new File(path);
        System.setProperty(MobileCapabilityType.APP, app.getAbsolutePath());
        System.setProperty(MobileCapabilityType.PLATFORM_NAME, "Android");
        System.setProperty(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        System.setProperty(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator1");

        RuntimeParameters.APP.refreshParameterValue();
        RuntimeParameters.PLATFORM_NAME.refreshParameterValue();
        RuntimeParameters.DEVICE_NAME.refreshParameterValue();
        RuntimeParameters.AUTOMATION_NAME.refreshParameterValue();

        PropertiesFileSettings propertiesFileSettings = Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesFileSettings.class);

        driverManager = new DriverManager(propertiesFileSettings);

    }

    @AfterEach
    public void tearDown() throws Exception {
        driverManager.stop();
    }

    @Test
    public void test() {
        assertTrue(true);
    }

}
