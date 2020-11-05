package com.capgemini.mrchecker.database.core;

import java.util.Objects;

import javax.persistence.EntityManager;

import com.capgemini.mrchecker.database.core.base.properties.PropertiesDatabase;
import com.capgemini.mrchecker.database.core.base.runtime.RuntimeParametersDatabase;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;

abstract public class BasePageDatabase extends Page implements IDatabasePrefixHolder {

    protected EntityManager entityManager = null;

    private static IEnvironmentService environmentService;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "Database-Module";

    private final static PropertiesDatabase PROPERTIES_DATABASE;

    static {
        // Get analytics instance created in BaseTest
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from selenium.settings file
        PROPERTIES_DATABASE = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersDatabase();

        // Read Environment variables either from environments.csv or any other input data.
        setEnvironmentInstance();
    }

    public BasePageDatabase() {
        entityManager = DriverManager.createEntityManager(getDatabaseUnitName());
    }

    public static IAnalytics getAnalytics() {
        return ANALYTICS;
    }

    @Override
    public void onTestClassFinish() {
        super.onTestClassFinish();
        closeSession();
        BFLogger.logDebug("Session for connection: [" + getDatabaseUnitName() + "] closed.");
    }

    private void closeSession() {
        if (!Objects.isNull(entityManager)) {
            entityManager.close();
            entityManager = null;
        }
        DriverManager.closeDriver();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }


    @Override
    public ModuleType getModuleType() {
        return ModuleType.DATABASE;
    }

    private static PropertiesDatabase setPropertiesSettings() {
        // Get and then set properties information from settings.properties file
        return Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesDatabase.class);
    }

    private static void setRuntimeParametersDatabase() {
        // Read System or maven parameters
        BFLogger.logDebug(java.util.Arrays.asList(RuntimeParametersDatabase.values()).toString());
    }

    private static void setEnvironmentInstance() {
        /*
         * Environment variables either from environments.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmentInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }
}