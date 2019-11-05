package com.capgemini.mrchecker.mobile.core.newDrivers;

import com.capgemini.mrchecker.mobile.core.base.driver.DriverManager;
import com.capgemini.mrchecker.mobile.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.google.inject.Guice;
import org.junit.*;

import static org.junit.Assert.assertTrue;

public class DriverManagerTest {
	
	private DriverManager driverManager;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
	public void setUp() throws Exception {
		
		PropertiesFileSettings propertiesFileSettings = Guice.createInjector(PropertiesSettingsModule.init())
				.getInstance(PropertiesFileSettings.class);
		
		driverManager = new DriverManager(propertiesFileSettings);
		
	}
	
	@After
	public void tearDown() throws Exception {
		driverManager.stop();
	}
	
	@Test
	public void test() {
		assertTrue(true);
	}
	
}
