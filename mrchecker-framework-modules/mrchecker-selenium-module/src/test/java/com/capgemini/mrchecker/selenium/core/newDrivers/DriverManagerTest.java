package com.capgemini.mrchecker.selenium.core.newDrivers;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.google.inject.Guice;
import org.junit.jupiter.api.*;

import static org.junit.Assert.assertTrue;

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
        driverManager.start();
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
