package com.capgemini.mrchecker.selenium.unit.base.properties;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.tags.UnitTest;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@UnitTest
@ResourceLock(value = "SingleThread")
public class PropertiesManagerTest {

    @BeforeAll
    public static void setUpBeforeClass() {
    }

    @AfterAll
    public static void tearDownAfterClass() {
    }

    private PropertiesSelenium propertiesSelenium;

    @BeforeEach
    public void setUp() {
        String path = System.getProperty("user.dir") + Paths.get("/src/test/resources/settings.properties");
        try {
            InputStream fileSource = new FileInputStream(path);

            Injector i = Guice.createInjector(PropertiesSettingsModule.init(fileSource));
            this.propertiesSelenium = i.getInstance(PropertiesSelenium.class);
        } catch (FileNotFoundException e) {
            throw new BFInputDataException("Default file not found in: " + path);
        }

    }

    @AfterEach
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
