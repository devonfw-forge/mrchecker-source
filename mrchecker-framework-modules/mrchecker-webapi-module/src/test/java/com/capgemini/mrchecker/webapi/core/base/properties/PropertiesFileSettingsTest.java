package com.capgemini.mrchecker.webapi.core.base.properties;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.webapi.tags.UnitTest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@UnitTest
@ResourceLock("PropertiesFileSettings.class")
public class PropertiesFileSettingsTest {

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    private PropertiesWebAPI propertiesFileSettings;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
        PropertiesSettingsModule.delInstance();
    }

    @Test
    public void testParameterEnableVirtualServer_True() throws FileNotFoundException {
        String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings.properties");
        Injector i = Guice.createInjector(PropertiesSettingsModule.init(new FileInputStream(path)));
        this.propertiesFileSettings = i.getInstance(PropertiesWebAPI.class);

        assertEquals("", true, propertiesFileSettings.isVirtualServerEnabled());
    }

    @Test
    public void testParameterEnableVirtualServer_False() throws FileNotFoundException {
        String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings2.properties");
        Injector i = Guice.createInjector(PropertiesSettingsModule.init(new FileInputStream(path)));
        this.propertiesFileSettings = i.getInstance(PropertiesWebAPI.class);

        assertEquals("", false, propertiesFileSettings.isVirtualServerEnabled());
    }

    @Test
    public void testParameterEnableVirtualServer_NoValue() throws FileNotFoundException {
        String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings3.properties");
        Injector i = Guice.createInjector(PropertiesSettingsModule.init(new FileInputStream(path)));
        this.propertiesFileSettings = i.getInstance(PropertiesWebAPI.class);

        assertEquals("", false, propertiesFileSettings.isVirtualServerEnabled());
    }

    @Test
    public void testParameterEnableVirtualServer_Text() throws FileNotFoundException {
        String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings4.properties");
        Injector i = Guice.createInjector(PropertiesSettingsModule.init(new FileInputStream(path)));
        this.propertiesFileSettings = i.getInstance(PropertiesWebAPI.class);

        assertEquals("", false, propertiesFileSettings.isVirtualServerEnabled());
    }

    @Test
    public void testParameterEnableVirtualServer_NoParameter() throws FileNotFoundException {
        String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings5.properties");
        Injector i = Guice.createInjector(PropertiesSettingsModule.init(new FileInputStream(path)));
        this.propertiesFileSettings = i.getInstance(PropertiesWebAPI.class);

        assertEquals("", true, propertiesFileSettings.isVirtualServerEnabled());
    }

    @Test
    public void testDefaultParameters() {
        PropertiesSettingsModule.delInstance();

        Injector i = Guice.createInjector(PropertiesSettingsModule.init());
        PropertiesWebAPI propertiesFileSettings = i.getInstance(PropertiesWebAPI.class);

        assertEquals("", true, propertiesFileSettings.isVirtualServerEnabled());
    }

}