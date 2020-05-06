package com.capgemini.mrchecker.mobile.core.base.properties;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.*;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class PropertiesManagerTest {

	@BeforeClass
	public static void setUpBeforeClass() {
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	private PropertiesFileSettings propertiesFileSettings;

	@Before
	public void setUp() {
		String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings.properties");
		Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
		this.propertiesFileSettings = i.getInstance(PropertiesFileSettings.class);
	}

	@After
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

	// @Ignore
	@Test
	public void testDefaultParamters() {
		PropertiesSettingsModule.delInstance();

		Injector i = Guice.createInjector(PropertiesSettingsModule.init());
		PropertiesFileSettings propertiesFileSettings = i.getInstance(PropertiesFileSettings.class);

		assertEquals("", "Hello One", propertiesFileSettings.getProperty_1());
	}

}
