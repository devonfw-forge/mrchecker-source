package com.capgemini.mrchecker.example.core;

import java.util.Objects;

import com.capgemini.mrchecker.example.core.base.driver.DriverManager;
import com.capgemini.mrchecker.example.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.example.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.Attachments;
import com.google.inject.Guice;

abstract public class BasePage extends Page {
	
	private static DriverManager driver = null;
	
	private final static PropertiesFileSettings	PROPERTIES_EXAMPLE;
	private static IEnvironmentService			environmentService;
	private final static IAnalytics				ANALYTICS;
	public final static String					analitycsCategoryName	= "NAME-OF-MODULE";	// Selenium-Module
	
	static {
		// Get analytics instance created in BaseTets
		ANALYTICS = BaseTest.getAnalytics();
		
		// Get and then set properties information from selenium.settings file
		PROPERTIES_EXAMPLE = setPropertiesSettings();
		
		// Read System or maven parameters
		setRuntimeParametersSelenium();
		
		// Read Environment variables either from environments.csv or any other input data.
		setEnvironmetInstance();
	}
	
	public static IAnalytics getAnalytics() {
		return BasePage.ANALYTICS;
	}
	
	public BasePage() {
		this(getDriver());
	}
	
	public BasePage(DriverManager driver) {
		driver.start();
	}
	
	@Override
	public void onTestFailure() {
		super.onTestFailure();
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
	public void onTestClassFinish() {
		super.onTestClassFinish();
		DriverManager.closeDriver();
	}
	
	@Override
	public ModuleType getModuleType() {
		return ModuleType.EXAMPLE;
	}
	
	public void makeScreenshotOnFailure() {
		Attachments.attachToAllure("");
	}
	
	public void makeSourcePageOnFailure() {
		Attachments.attachToAllure("");
	}
	
	public static DriverManager getDriver() {
		if (Objects.isNull(driver)) {
			// Create module driver
			driver = new DriverManager(PROPERTIES_EXAMPLE);
		}
		return driver;
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
	
	private static void setEnvironmetInstance() {
		/*
		 * Environment variables either from environmnets.csv or any other input data. For now there is no properties
		 * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
		 * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
		 */
		
	}
	
}
