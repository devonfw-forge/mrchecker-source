package com.capgemini.mrchecker.webapi.core;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.webapi.core.base.runtime.RuntimeParameters;
import com.google.inject.Guice;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

import java.util.Objects;

public abstract class BaseEndpoint extends Page implements IWebAPI {
    private static DriverManager driver = null;
    private final static PropertiesFileSettings propertiesFileSettings;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "WebAPI-Module";

    static {
        // Get analytics instance created in BaseTets
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from selenium.settings file
        propertiesFileSettings = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersWebApi();

        // Read Environment variables either from environmnets.csv or any other input data.
        setEnvironmentInstance();
    }

    public static IAnalytics getAnalytics() {
        return ANALYTICS;
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.WEBAPI;
    }

    public static RequestSpecification getDriver() {
        if (Objects.isNull(driver)) {
            // Create module driver
            driver = new DriverManager(propertiesFileSettings);
        }
        return driver.getDriverWebAPI();
    }

    public static RequestSpecification getDriver(RestAssuredConfig config) {
        if (Objects.isNull(driver)) {
            // Create module driver
            driver = new DriverManager(propertiesFileSettings);
        }
        return driver.getDriverWebAPI(config);
    }

    private static PropertiesFileSettings setPropertiesSettings() {
        // Get and then set properties information from settings.properties file
        return Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesFileSettings.class);
    }

    private static void setRuntimeParametersWebApi() {
        // Read System or maven parameters
        BFLogger.logDebug(java.util.Arrays.asList(RuntimeParameters.values())
                .toString());
    }

    private static void setEnvironmentInstance() {
        /*
         * Environment variables either from environmnets.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }
}