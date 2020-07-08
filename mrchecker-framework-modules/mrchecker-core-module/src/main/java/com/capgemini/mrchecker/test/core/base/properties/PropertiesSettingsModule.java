package com.capgemini.mrchecker.test.core.base.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

@Singleton
public class PropertiesSettingsModule extends AbstractModule {
	
	private static final String DEFAULT_FILE_SOURCE_FILE_PATH = System.getProperty("user.dir") + Paths.get("/src/resources/settings.properties");
	
	private static PropertiesSettingsModule	instance;
	private final ThreadLocal<InputStream>	propertiesSources	= new ThreadLocal<>();
	
	@Override
	protected void configure() {
		try (InputStream propertiesSource = propertiesSources.get()) {
			Properties properties = new Properties();
			properties.load(propertiesSource);
			Names.bindProperties(binder(), properties);
		} catch (IOException e) {
			throw new BFInputDataException("Could not read from properties input source: " + propertiesSources.get()
					.toString());
		}
	}
	
	public static PropertiesSettingsModule init() {
		try {
			InputStream fileSource = new FileInputStream(DEFAULT_FILE_SOURCE_FILE_PATH);
			return PropertiesSettingsModule.init(fileSource);
		} catch (FileNotFoundException e) {
			throw new BFInputDataException("Default file not found in: " + DEFAULT_FILE_SOURCE_FILE_PATH);
		}
	}
	
	public static PropertiesSettingsModule init(InputStream propertiesSource) {
		if (Objects.isNull(instance)) {
			synchronized (PropertiesSettingsModule.class) {
				if (Objects.isNull(instance)) {
					instance = new PropertiesSettingsModule();
				}
			}
		}
		
		instance.propertiesSources.set(propertiesSource);
		BFLogger.logDebug("Properties settings source=" + propertiesSource.toString());
		
		return instance;
	}
	
	public static void delInstance() {
		PropertiesSettingsModule.instance = null;
	}
}
