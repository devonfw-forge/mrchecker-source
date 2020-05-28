package com.capgemini.mrchecker.mobile.core.base.properties;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

@ResourceLock("PropertiesFileSettings.class")
public class PropertiesManagerTest {
	
	private PropertiesFileSettings propertiesFileSettings;
	
	@BeforeEach
	public void setUp() throws FileNotFoundException {
		String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings.properties");
		Injector i = Guice.createInjector(PropertiesSettingsModule.init(new FileInputStream(path)));
		this.propertiesFileSettings = i.getInstance(PropertiesFileSettings.class);
	}
	
	@AfterEach
	public void tearDown() {
		PropertiesSettingsModule.delInstance();
	}
	
	@Test
	public void testParamter_1() {
		assertEquals("", "This is value for prop 1", propertiesFileSettings.getProperty_1());
	}
	
	@Test
	public void testParamter_2() {
		assertEquals("", "This is value for prop 2", propertiesFileSettings.getProperty_2());
	}
	
	@Test
	public void testDefaultParamters() {
		PropertiesSettingsModule.delInstance();
		
		Injector i = Guice.createInjector(PropertiesSettingsModule.init());
		PropertiesFileSettings propertiesFileSettings = i.getInstance(PropertiesFileSettings.class);
		
		assertEquals("", "Hello One", propertiesFileSettings.getProperty_1());
	}
}
