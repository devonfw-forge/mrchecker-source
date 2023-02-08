package com.capgemini.mrchecker.test.core;

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
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Objects;

public abstract class BaseTest {
    private static PropertiesCoreTest properties;
    private static IEnvironmentService environmentService;
    private static IAnalytics analytics;

    @RegisterExtension
    @SuppressWarnings("unused")
    public static final ITestExecutionObserver TEST_EXECUTION_OBSERVER = TestExecutionObserver.getInstance();

    static {
        setProperties();
    }

    private static PropertiesCoreTest getProperties() {
        return properties;
    }

    private static void setProperties() {
        // Get and then set properties information from settings.properties file
        properties = Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesCoreTest.class);
    }

    public static IEnvironmentService getEnvironmentService() {
        if (Objects.isNull(environmentService)) {
            setEnvironmentService();
        }
        return environmentService;
    }

    private static synchronized void setEnvironmentService() {
        if (Objects.isNull(environmentService)) {
            setRuntimeParametersCore(getProperties().getDefaultEnvironmentName());
            setEnvironmentInstance(getProperties().isEncryptionEnabled());
        }
    }

    public static synchronized void setEnvironmentService(IEnvironmentService environmentService) {
        BaseTest.environmentService = environmentService;
    }

    private static synchronized void setRuntimeParametersCore(String defaultEnvironmentName) {
        RuntimeParametersCore.ENV.setDefaultValue(defaultEnvironmentName);
        RuntimeParametersCore.ENV.refreshParameterValue();
        BFLogger.logDebug(RuntimeParametersCore.ENV.toString());
    }

    private static synchronized void setEnvironmentInstance(boolean isEncryptionEnabled) {
        // Environment variables either from environments.csv or any other input data.
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

    public static IAnalytics getAnalytics() {
        if (Objects.isNull(analytics)) {
            setAnalytics();
        }
        return analytics;
    }

    private static synchronized void setAnalytics() {
        if (Objects.isNull(analytics)) {
            boolean isAnalyticsEnabled = getProperties().isAnalyticsEnabled();
            BFLogger.logAnalytics("Is analytics enabled: " + isAnalyticsEnabled);
            analytics = isAnalyticsEnabled ? AnalyticsProvider.DISABLED : AnalyticsProvider.DISABLED;
        }
    }

    public void setUp() {
    }

    public void tearDown() {
    }
}