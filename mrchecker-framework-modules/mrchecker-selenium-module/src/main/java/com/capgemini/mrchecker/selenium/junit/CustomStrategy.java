package com.capgemini.mrchecker.selenium.junit;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

//Used in Selenium test project pom surefire config to properly run tests parallel on JUnit 5 + Selenium 4
//junit.jupiter.execution.parallel.config.strategy = custom
//junit.jupiter.execution.parallel.config.custom.class=com.capgemini.mrchecker.selenium.junit.CustomStrategy
public class CustomStrategy implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {
    private int threadCount = Integer.parseInt(System.getProperty("thread.count", "1"));

    @Override
    public int getParallelism() {
        return threadCount;
    }

    @Override
    public int getMinimumRunnable() {
        return 0;
    }

    @Override
    public int getMaxPoolSize() {
        return threadCount;
    }

    @Override
    public int getCorePoolSize() {
        return threadCount;
    }

    @Override
    public int getKeepAliveSeconds() {
        return threadCount;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        return this;
    }
}