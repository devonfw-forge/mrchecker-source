package com.capgemini.mrchecker.test.core.base.environment;

import com.capgemini.mrchecker.test.core.base.environment.providers.SpreadsheetEnvironmentService;
import com.capgemini.mrchecker.test.core.base.runtime.RuntimeParametersCore;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.nio.file.Paths;

public class EnvironmentModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	IEnvironmentService provideSpreadsheetEnvironmentService() {
		String path = System.getProperty("user.dir") + Paths.get("/src/resources/enviroments/environments.csv");
		String environment = RuntimeParametersCore.ENV.getValue();
		SpreadsheetEnvironmentService.init(path, environment);
		return SpreadsheetEnvironmentService.getInstance();
	}
}