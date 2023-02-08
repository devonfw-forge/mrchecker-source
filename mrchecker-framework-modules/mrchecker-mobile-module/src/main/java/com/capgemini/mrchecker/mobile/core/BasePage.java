package com.capgemini.mrchecker.mobile.core;

import com.capgemini.mrchecker.mobile.core.base.driver.DriverManager;
import com.capgemini.mrchecker.mobile.core.base.driver.INewMobileDriver;
import com.capgemini.mrchecker.mobile.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.mobile.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;

import java.util.Objects;

abstract public class BasePage extends Page {

    private static DriverManager driver = null;

    private final static PropertiesFileSettings PROPERTIES_FILE_SETTINGS;
    private static IEnvironmentService environmentService;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "Mobile";

    static {
        // Get analytics instance created in BaseTets
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from mobile.settings file
        PROPERTIES_FILE_SETTINGS = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersSelenium();

        // Read Environment variables either from environments.csv or any other input data.
        setEnvironmentInstance();
    }

    public static IAnalytics getAnalytics() {
        return ANALYTICS;
    }

    public BasePage() {
        getDriver();
    }

    @Override
    public void onTestExecutionException() {
        super.onTestExecutionException();
        makeScreenshotOnFailure();
        makeSourcePageOnFailure();
    }

    @Override
    public void onSetupFailure() {
        super.onSetupFailure();
        makeScreenshotOnFailure();
        makeSourcePageOnFailure();
    }

    @Override
    public void onTeardownFailure() {
        super.onTeardownFailure();
        makeScreenshotOnFailure();
        makeSourcePageOnFailure();
    }

    @Override
    public void onTestClassFinish() {
        super.onTestClassFinish();
        DriverManager.closeDriver();
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.MOBILE;
    }

    @Attachment("Screenshot on failure")
    public byte[] makeScreenshotOnFailure() {
        byte[] screenshot = null;
        try {
            screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (UnhandledAlertException e) {
            BFLogger.logDebug("[makeScreenshotOnFailure] Unable to take screenshot.");
        }
        return screenshot;
    }

    @Attachment("Source Page on failure")
    public String makeSourcePageOnFailure() {
        return DriverManager.getDriver()
                .getPageSource();
    }

    public static INewMobileDriver getDriver() {
        if (Objects.isNull(driver)) {
            // Create module driver
            driver = new DriverManager(PROPERTIES_FILE_SETTINGS);
        }

        return driver.getDriver();

    }

    private static PropertiesFileSettings setPropertiesSettings() {
        // Get and then set properties information from settings.properties file
        return Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesFileSettings.class);
    }

    private static void setRuntimeParametersSelenium() {
        // Read System or maven parameters
        BFLogger.logDebug(java.util.Arrays.asList(RuntimeParameters.values())
                .toString());

    }

    private static void setEnvironmentInstance() {
        /*
         * Environment variables either from environments.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */

    }
}