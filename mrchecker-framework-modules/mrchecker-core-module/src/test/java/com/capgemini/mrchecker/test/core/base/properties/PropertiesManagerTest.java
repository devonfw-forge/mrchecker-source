package com.capgemini.mrchecker.test.core.base.properties;

public class PropertiesManagerTest {
	
	// @BeforeClass
	// public static void setUpBeforeClass() throws Exception {
	// }
	//
	// @AfterClass
	// public static void tearDownAfterClass() throws Exception {
	// }
	//
	// private PropertiesCoreTest properties;
	//
	// @Before
	// public void setUp() throws Exception {
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// this.properties = i.getInstance(PropertiesCoreTest.class);
	// }
	//
	// @After
	// public void tearDown() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// }
	//
	// @Test
	// public void testParamterIsAnalyticsEnabled() {
	// assertEquals("", true, properties.isAnalyticsEnabled());
	// }
	//
	// @Test
	// public void testParamterIsEncryptionEnabled() {
	// assertEquals("", true, properties.isEncryptionEnabled());
	// }
	//
	// @Test
	// public void testParamterIsAnalyticsEnabledWithUnkonwText() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings3.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertTrue("", properties.isAnalyticsEnabled());
	// }
	//
	// @Test
	// public void testParamterIsEncryptionEnabledWithUnkonwText() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings3.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertFalse("", properties.isEncryptionEnabled());
	// }
	//
	// @Test
	// public void testParamterIsAnalyticsEnabledWithFalse() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings2.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertFalse("", properties.isAnalyticsEnabled());
	// }
	//
	// @Test
	// public void testParamterIsEncryptionEnabledWithFalse() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings2.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertFalse("", properties.isEncryptionEnabled());
	// }
	//
	// @Test
	// public void testDefaultParamters() {
	// PropertiesSettingsModule.delInstance();
	//
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init());
	// PropertiesCoreTestFake properties = i.getInstance(PropertiesCoreTestFake.class);
	//
	// assertEquals("", true, properties.doesNotExist());
	// }
	//
	// @Test
	// public void testParamterIsAnalyticsEnabledNoValue() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings4.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertTrue("", properties.isAnalyticsEnabled());
	// }
	//
	// @Test
	// public void testParamterIsEncryptionEnabledNoValue() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings4.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertFalse("", properties.isEncryptionEnabled());
	// }
	//
	// @Test
	// public void testParamterDefaultEnvironmentNameNoValue() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings4.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertEquals("DEV", properties.getDefaultEnvironmentName());
	// }
	//
	// @Test
	// public void testParamterDefaultEnvironmentNameNonDefault() throws Exception {
	// PropertiesSettingsModule.delInstance();
	// String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings2.properties");
	// Injector i = Guice.createInjector(PropertiesSettingsModule.init(path));
	// properties = i.getInstance(PropertiesCoreTest.class);
	//
	// assertEquals("NON_DEFAULT", properties.getDefaultEnvironmentName());
	// }
	//
	// private static class PropertiesCoreTestFake {
	//
	// private Boolean coreDoesNotExist = true;
	//
	// @Inject(optional = true)
	// private void setDoesNotExist(@Named("core.doesNotExist") Boolean status) {
	// this.coreDoesNotExist = status;
	//
	// }
	//
	// public Boolean doesNotExist() {
	// return this.coreDoesNotExist;
	// }
	//
	// }
	
}
