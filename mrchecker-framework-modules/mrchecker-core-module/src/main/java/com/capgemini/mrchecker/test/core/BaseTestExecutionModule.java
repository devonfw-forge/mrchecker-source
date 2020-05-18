package com.capgemini.mrchecker.test.core;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class BaseTestExecutionModule extends AbstractModule {
	
	@Override
	protected void configure() {
	}
	
	@Provides
	@Singleton
	public ITestExecutionObserver getInstance() {
		return new BaseTestExecutionObserver();
	}
}
