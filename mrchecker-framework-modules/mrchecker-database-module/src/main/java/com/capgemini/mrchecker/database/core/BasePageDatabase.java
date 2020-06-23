package com.capgemini.mrchecker.database.core;

import java.util.Objects;

import javax.persistence.EntityManager;

import com.capgemini.mrchecker.database.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.database.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;

import lombok.Getter;

abstract public class BasePageDatabase extends Page implements IDatabasePrefixHolder {
	
	private final static PropertiesFileSettings	PROPERTIES_FILE_SETTINGS;
	private static IEnvironmentService			environmentService;
	private final static IAnalytics				ANALYTICS;
	
	@Getter
	protected EntityManager entityManager = null;
	
	public final static String analyticsCategoryName = "Database-Module";
	
	static {
		// Get analytics instance created in BaseTest
		ANALYTICS = BaseTest.getAnalytics();
		
		// Get and then set properties information from selenium.settings file
		PROPERTIES_FILE_SETTINGS = setPropertiesSettings();
		
		// Read System or maven parameters
		setRuntimeParametersDatabase();
		
		// Read Environment variables either from environments.csv or any other input data.
		setEnvironmentInstance();
	}
	
	public BasePageDatabase() {
		assignEntityManager();
	}
	
	public static IAnalytics getANALYTICS() {
		return ANALYTICS;
	}
	
	@Override
	public void onTestFailure() {
		super.onTestFailure();
		closeSession();
	}
	
	@Override
	public void onTestClassFinish() {
		closeSession();
		BFLogger.logDebug("Session for connection: [" + getDatabaseUnitName() + "] closed.");
		super.onTestClassFinish();
	}
	
	@Override
	public ModuleType getModuleType() {
		return ModuleType.DATABASE;
	}
	
	private static PropertiesFileSettings setPropertiesSettings() {
		// Get and then set properties information from settings.properties file
		return Guice.createInjector(PropertiesSettingsModule.init())
				.getInstance(PropertiesFileSettings.class);
	}
	
	private static void setRuntimeParametersDatabase() {
		// Read System or maven parameters
		BFLogger.logDebug(java.util.Arrays.asList(RuntimeParameters.values())
				.toString());
		
	}
	
	private void closeSession() {
		if (!Objects.isNull(entityManager)) {
			entityManager.close();
		}
	}
	
	private void assignEntityManager() {
		if (Objects.isNull(entityManager)) {
			entityManager = DriverManager.createEntityManager(getDatabaseUnitName());
		}
	}
	
	private static void setEnvironmentInstance() {
		/*
		 * Environment variables either from environmnets.csv or any other input data. For now there is no properties
		 * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
		 * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
		 */
		
	}
	
}