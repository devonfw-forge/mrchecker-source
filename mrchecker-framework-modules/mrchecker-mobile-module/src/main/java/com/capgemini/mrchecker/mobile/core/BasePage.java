package com.capgemini.mrchecker.mobile.core;



import com.capgemini.mrchecker.mobile.core.base.driver.DriverManager;
import com.capgemini.mrchecker.mobile.core.base.driver.INewMobileDriver;
import com.capgemini.mrchecker.mobile.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.mobile.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.BaseTestWatcher;
import com.capgemini.mrchecker.test.core.ITestObserver;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;

abstract public class BasePage implements ITestObserver {
	
	private static DriverManager driver = null;
	
	private final static PropertiesFileSettings propertiesFileSettings;
	private static IEnvironmentService environmentService;
	private final static IAnalytics analytics;
	public final static String analitycsCategoryName = "Mobile";
	
	static {
		// Get analytics instance created in BaseTets
		analytics = BaseTest.getAnalytics();
		
		// Get and then set properties information from mobile.settings file
		propertiesFileSettings = setPropertiesSettings();
		
		// Read System or maven parameters
		setRuntimeParametersSelenium();
		
		// Read Environment variables either from environmnets.csv or any other input data.
		setEnvironmetInstance();
	}
	
	public static IAnalytics getAnalytics() {
		return BasePage.analytics;
	}
	
	public BasePage() {
		this(getDriver());
	}
	
	public BasePage(INewMobileDriver driver) {
		// Add given module to Test core Observable list
		this.addObserver();
		
	}
	
	@Override
	public void addObserver() {
		BaseTestWatcher.addObserver(this);
	}
	
	@Override
	public void onTestFailure() {
		BFLogger.logDebug("BasePage.onTestFailure    " + this.getClass()
				.getSimpleName());
		makeScreenshotOnFailure();
		makeSourcePageOnFailure();
	}
	
	@Override
	public void onTestSuccess() {
		// All actions needed while test method is success
		BFLogger.logDebug("BasePage.onTestSuccess    " + this.getClass()
				.getSimpleName());
	}
	
	@Override
	public void onTestFinish() {
		// All actions needed while test class is finishing
		BFLogger.logDebug("BasePage.onTestFinish   " + this.getClass()
				.getSimpleName());
		BaseTestWatcher.removeObserver(this);
	}
	
	@Override
	public void onTestClassFinish() {
		BFLogger.logDebug("BasePage.onTestClassFinish   " + this.getClass()
				.getSimpleName());
		BFLogger.logDebug("driver:" + getDriver().toString());
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
		if (BasePage.driver == null) {
			// Create module driver
			BasePage.driver = new DriverManager(propertiesFileSettings);
		}
		return BasePage.driver.getDriver();
		
	}

	private static PropertiesFileSettings setPropertiesSettings() {
		// Get and then set properties information from settings.properties file
		PropertiesFileSettings propertiesFileSettings = Guice.createInjector(PropertiesSettingsModule.init())
				.getInstance(PropertiesFileSettings.class);
		return propertiesFileSettings;
	}
	
	private static void setRuntimeParametersSelenium() {
		// Read System or maven parameters
		BFLogger.logDebug(java.util.Arrays.asList(RuntimeParameters.values())
				.toString());
		
	}
	
	private static void setEnvironmetInstance() {
		/*
		 * Environment variables either from environmnets.csv or any other input data. For now there is no properties
		 * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
		 * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
		 */
		
	}
	
}
