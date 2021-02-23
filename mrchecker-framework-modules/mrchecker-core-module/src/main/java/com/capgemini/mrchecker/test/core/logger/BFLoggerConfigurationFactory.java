package com.capgemini.mrchecker.test.core.logger;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.AsyncLoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.xml.sax.InputSource;

/**
 * https://logging.apache.org/log4j/log4j-2.4/manual/customconfig.html
 * Combining Configuration File with Programmatic Configuration
 */
@Plugin(name = "BFLoggerConfigurationFactory", category = "ConfigurationFactory")
@Order(10)
public class BFLoggerConfigurationFactory extends ConfigurationFactory {
    public static final String[] SUFFIXES = new String[] {".xml", "*"};
//    public Configuration getConfiguration(InputSource source) {
//        return new BFLoggerConfiguration(source, configFile);
//    }
    @Override
    public String[] getSupportedTypes() {
        return SUFFIXES;
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        return new BFLoggerConfiguration(loggerContext,source);
    }
}
