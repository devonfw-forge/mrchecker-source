package com.capgemini.mrchecker.test.core;

import org.junit.jupiter.api.extension.RegisterExtension;

import com.capgemini.mrchecker.test.core.analytics.AnalyticsProvider;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.encryption.DataEncryptionModule;
import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.environment.EnvironmentModule;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesCoreTest;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.base.runtime.RuntimeParametersCore;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;

public abstract class BaseTest {
	private static PropertiesCoreTest propertiesCoreTest;
	
	@RegisterExtension
	@SuppressWarnings("unused")
	public static final ITestExecutionObserver TEST_EXECUTION_OBSERVER = TestExecutionObserver.getInstance();
	
	private static IEnvironmentService	environmentService;
	private static IAnalytics			analytics;
	
	static {
		setProperties();
		setRuntimeParametersCore(propertiesCoreTest.getDefaultEnvironmentName());
		setEnvironmentInstance(propertiesCoreTest.isEncryptionEnabled());
		setAnalytics(propertiesCoreTest.isAnalyticsEnabled());
	}
	
	public static IEnvironmentService getEnvironmentService() {
		return environmentService;
	}
	
	public static void setEnvironmentService(IEnvironmentService environmentService) {
		BaseTest.environmentService = environmentService;
	}
	
	public static IAnalytics getAnalytics() {
		return analytics;
	}
	
	private static void setRuntimeParametersCore(String defaultEnvironmentName) {
		RuntimeParametersCore.ENV.setDefaultValue(defaultEnvironmentName);
		RuntimeParametersCore.ENV.refreshParameterValue();
		BFLogger.logDebug(RuntimeParametersCore.ENV.toString());
	}
	
	private static void setProperties() {
		/*
		 * For now there is no properties settings file for Core module. In future, please have a look on Selenium
		 * Module PropertiesSelenium propertiesSelenium = Guice.createInjector(PropertiesSettingsModule.init())
		 * .getInstance(PropertiesSelenium.class);
		 */
		
		// Get and then set properties information from settings.properties file
		propertiesCoreTest = Guice.createInjector(PropertiesSettingsModule.init())
				.getInstance(PropertiesCoreTest.class);
	}
	
	private static void setEnvironmentInstance(boolean isEncryptionEnabled) {
		// Environment variables either from n.csv or any other input data.
		IEnvironmentService environmentInstance = Guice.createInjector(new EnvironmentModule())
				.getInstance(IEnvironmentService.class);
		environmentInstance.setEnvironment(RuntimeParametersCore.ENV.getValue());
		if (isEncryptionEnabled) {
			IDataEncryptionService encryptionService = Guice.createInjector(new DataEncryptionModule())
					.getInstance(IDataEncryptionService.class);
			environmentInstance.setDataEncryptionService(encryptionService);
		}
		setEnvironmentService(environmentInstance);
	}
	
	private static void setAnalytics(Boolean isAnalyticsEnabled) {
		BFLogger.logAnalytics("Is analytics enabled: " + isAnalyticsEnabled);
		analytics = isAnalyticsEnabled ? AnalyticsProvider.DISABLED : AnalyticsProvider.DISABLED;
	}
	
	public void setUp() {
	}
	
	public void tearDown() {
	}
}