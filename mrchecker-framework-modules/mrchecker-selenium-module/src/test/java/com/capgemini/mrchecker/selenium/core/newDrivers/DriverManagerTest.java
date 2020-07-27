package com.capgemini.mrchecker.selenium.core.newDrivers;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.google.inject.Guice;

@Disabled
public class DriverManagerTest {
	
	private DriverManager driverManager;
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
	}
	
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		
		PropertiesSelenium propertiesSelenium = Guice.createInjector(PropertiesSettingsModule.init())
				.getInstance(PropertiesSelenium.class);
		
		driverManager = new DriverManager(propertiesSelenium);
		
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		driverManager.stop();
	}
	
	@Test
	public void test() {
		assertTrue(true);
	}
	
}
