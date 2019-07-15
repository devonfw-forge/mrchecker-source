package com.capgemini.mrchecker.selenium.core.base.properties;

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

	private PropertiesSelenium propertiesSelenium;

	@Before
	public void setUp() {
		String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings.properties");
		Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
		this.propertiesSelenium = i.getInstance(PropertiesSelenium.class);
	}

	@After
	public void tearDown() {
		PropertiesSettingsModule.delInstance();
	}

	@Test
	public void testParamterGetChrome() {
		assertEquals("", "chromedriver.exe", propertiesSelenium.getSeleniumChrome());
	}

	@Test
	public void testParamterGetFirefox() {
		assertEquals("", "geckodriver.exe", propertiesSelenium.getSeleniumFirefox());
	}

	@Test
	public void testParamterGetIE() {
		assertEquals("", "IEDriverServer.exe", propertiesSelenium.getSeleniumIE());
	}

	@Test
	public void testParamterGetEdge() {
		assertEquals("", "edgedriver.exe", propertiesSelenium.getSeleniumEdge());
	}

	@Test
	public void testParamterGetOpera() {
		assertEquals("", "operadriver.exe", propertiesSelenium.getSeleniumOpera());
	}

	// @Ignore
	@Test
	public void testDefaultParamters() {
		PropertiesSettingsModule.delInstance();

		Injector i = Guice.createInjector(PropertiesSettingsModule.init());
		PropertiesSelenium propertiesSelenium = i.getInstance(PropertiesSelenium.class);

		assertEquals("", "./lib/webdrivers/chrome/chromedriver.exe", propertiesSelenium.getSeleniumChrome());
	}

}
