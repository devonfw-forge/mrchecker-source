package com.capgemini.mrchecker.security.core;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.EnvironmentModule;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.google.inject.Guice;

abstract public class BasePage extends Page {
	
	private static IEnvironmentService	environmentService;
	private final static IAnalytics		ANALYTICS;
	public final static String			ANALYTICS_CATEGORY_NAME	= "Security-Module";
	
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
	public ModuleType getModuleType() {
		return ModuleType.SECURITY;
	}
	
	private static void setEnvironmentInstance() {
		environmentService = Guice.createInjector(new EnvironmentModule())
				.getInstance(IEnvironmentService.class);
	}
}
