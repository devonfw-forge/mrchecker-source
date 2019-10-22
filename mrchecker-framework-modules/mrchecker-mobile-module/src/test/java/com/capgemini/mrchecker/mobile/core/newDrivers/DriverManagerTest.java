package com.capgemini.mrchecker.mobile.core.newDrivers;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.core.newDrivers.DriverManager;
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
		
		PropertiesSelenium propertiesSelenium = Guice.createInjector(PropertiesSettingsModule.init())
				.getInstance(PropertiesSelenium.class);
		
		driverManager = new DriverManager(propertiesSelenium);
		
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
