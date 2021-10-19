package com.capgemini.mrchecker.test.core.logger;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

public class BFLoggerConfiguration extends XmlConfiguration {
	public BFLoggerConfiguration(LoggerContext context, ConfigurationSource configSource) {
		super(context, configSource);
	}
}
