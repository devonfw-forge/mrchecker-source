package com.capgemini.mrchecker.security.core;

import com.capgemini.mrchecker.test.core.*;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.EnvironmentModule;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;

abstract public class BasePage implements IPage, ITestObserver {
	
	private static IEnvironmentService	environmentService;
	private final static IAnalytics		ANALYTICS;
	public final static String			ANALYTICS_CATEGORY_NAME	= "Security-Module";
	
	private static final ITestExecutionObserver TEST_EXECUTION_OBSERVER = BaseTestExecutionObserver.getInstance();
	
	private boolean isInitialized = false;
	
	static {
		// Get analytics instance created in BaseTets
		ANALYTICS = BaseTest.getAnalytics();
		
		// Read Environment variables either from environmnets.csv or any other input data.
		setEnvironmentInstance();
	}
	
	public static IAnalytics getAnalytics() {
		return ANALYTICS;
	}
	
	@Override
	public final void initialize() {
		TEST_EXECUTION_OBSERVER.addObserver(this);
		isInitialized = true;
	}
	
	@Override
	public final boolean isInitialized() {
		return isInitialized;
	}
	
	@Override
	public void onTestFailure() {
		BFLogger.logDebug("BasePage.onTestFailure    " + getClass()
				.getSimpleName());
	}
	
	@Override
	public void onTestSuccess() {
		// All actions needed while test method is success
		BFLogger.logDebug("BasePage.onTestSuccess    " + getClass()
				.getSimpleName());
	}
	
	@Override
	public void onTestFinish() {
		// All actions needed while test class is finishing
		BFLogger.logDebug("BasePage.onTestFinish   " + this.getClass()
				.getSimpleName());
		TEST_EXECUTION_OBSERVER.removeObserver(this);
	}
	
	@Override
	public void onTestClassFinish() {
		BFLogger.logDebug("BasePage.onTestClassFinish   " + this.getClass()
				.getSimpleName());
	}
	
	@Override
	public ModuleType getModuleType() {
		return ModuleType.SECURITY;
	}
	
	private static void setEnvironmentInstance() {
		environmentService = Guice.createInjector(new EnvironmentModule())
				.getInstance(IEnvironmentService.class);
	}
}
