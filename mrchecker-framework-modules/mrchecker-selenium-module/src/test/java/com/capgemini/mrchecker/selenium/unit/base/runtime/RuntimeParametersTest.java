package com.capgemini.mrchecker.selenium.unit.base.runtime;

import com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium;
import com.capgemini.mrchecker.selenium.tags.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium.INVALID_IE_NAME;
import static com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium.VALID_IE_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.fail;

@UnitTest
@ResourceLock(value = "SingleThread")
public class RuntimeParametersTest {

    private static final Map<String, String> STARTUP_PARAMETERS_VALUES = new LinkedHashMap<String, String>() {
        {
            put("browser", "magicbrowser");
            put("browserVersion", "11.0");
            put("seleniumGrid", "smth");
            put("os", "linux");
            put("browserOptions",
                    "headless;window-size=1200x600;testEquals=FirstEquals=SecondEquals;--testMe;acceptInsecureCerts=true;maxInstances=3");
        }
    };

    public static final String DEFAULT_BROWSER_VALUE = "chrome";
    public static final String BROWSER_OPTIONS_TEST_KEY = "browserOptionsTestKey";

    @BeforeEach
    public void setUp() {
        STARTUP_PARAMETERS_VALUES.forEach(System::setProperty);
        refreshAllParameters();
    }

    @AfterEach
    public void tearDown() {
        STARTUP_PARAMETERS_VALUES.forEach((k, v) -> System.clearProperty(k));
        refreshAllParameters();
    }

    private static void refreshAllParameters() {
        doForEachParam(RuntimeParametersSelenium::refreshParameterValue);
    }

    private static void doForEachParam(Consumer<RuntimeParametersSelenium> applyFunction) {
        Arrays.stream(RuntimeParametersSelenium.values())
                .forEach(applyFunction);
    }

    @Test
    public void shouldGetAllStartupProperties() {
        doForEachParam((e) -> assertThat(e.toString() + " failed!", e.getValue(), is(equalTo(STARTUP_PARAMETERS_VALUES.get(e.getKey())))));
    }

    @Test
    public void shouldGetReturnLowercaseBrowserValue() {
        System.setProperty("browser", STARTUP_PARAMETERS_VALUES.get("browser")
                .toUpperCase());

        refreshAllParameters();
        String browserValue = RuntimeParametersSelenium.BROWSER.getValue();

        assertThat(browserValue, is(equalTo(STARTUP_PARAMETERS_VALUES.get(RuntimeParametersSelenium.BROWSER.getKey()))));
    }

    @Test
    public void shouldGetReturnDefaultBrowserValue() {
        Arrays.stream(new String[]{"", "null"})
                .forEach((s) -> {
                    System.setProperty("browser", s);

                    refreshAllParameters();
                    String browserValue = RuntimeParametersSelenium.BROWSER.getValue();

                    assertThat("Failed for '" + s + "'!", browserValue, is(equalTo(DEFAULT_BROWSER_VALUE)));
                });
    }

    @Test
    public void shouldGetReturnDefaultBrowserValueWhenNoSystemProperty() {
        System.clearProperty("browser");

        refreshAllParameters();
        String browserValue = RuntimeParametersSelenium.BROWSER.getValue();

        assertThat(browserValue, is(equalTo(DEFAULT_BROWSER_VALUE)));
    }

    @Test
    public void shouldGetValidIEName() {
        System.setProperty("browser", INVALID_IE_NAME);

        refreshAllParameters();
        String browserValue = RuntimeParametersSelenium.BROWSER.getValue();

        assertThat(browserValue, is(equalTo(VALID_IE_NAME)));

    }

    @Test
    public void shouldBrowserOptionBeBoolean() {
        shouldBrowserOptionBeOfClass("true", Boolean.class);
    }

    @Test
    public void shouldBrowserOptionBeInteger() {
        shouldBrowserOptionBeOfClass("123", Integer.class);
    }

    @Test
    public void shouldBrowserOptionBeIntegerFloat() {
        shouldBrowserOptionBeOfClass("123.321", Float.class);
    }

    @Test
    public void shouldBrowserOptionBeIntegerString() {
        shouldBrowserOptionBeOfClass("blue", String.class);
    }

    private void shouldBrowserOptionBeOfClass(String value, Class<?> clazz) {
        System.setProperty("browserOptions", BROWSER_OPTIONS_TEST_KEY + "=" + value);

        refreshAllParameters();
        Map<String, Object> browserOptionsValues = RuntimeParametersSelenium.BROWSER_OPTIONS.getValues();
        Object optionValue = browserOptionsValues.get(BROWSER_OPTIONS_TEST_KEY);

        assertThat(optionValue, is(instanceOf(clazz)));
    }

    @Test
    public void shouldGetProperBrowserOptionsValuesSize() {
        final String[] browserOptions = STARTUP_PARAMETERS_VALUES.get(RuntimeParametersSelenium.BROWSER_OPTIONS.getKey())
                .split(";");

        int browserOptionsCount = RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
                .size();

        assertThat(browserOptionsCount, is(equalTo(browserOptions.length)));
    }

    @Test
    public void shouldGetProperBrowserOptionsValuesContents() {
        final String[] browserOptions = STARTUP_PARAMETERS_VALUES.get(RuntimeParametersSelenium.BROWSER_OPTIONS.getKey())
                .split(";");

        Map<String, Object> browserOptionsValues = RuntimeParametersSelenium.BROWSER_OPTIONS.getValues();

        browserOptionsValues.forEach((key, value) -> {
            try {
                String browserOption = Arrays.stream(browserOptions)
                        .filter(s -> s.startsWith(key))
                        .findFirst()
                        .orElseThrow(Exception::new);

                assertThat(browserOption.endsWith(value.toString()), is(equalTo(true)));
            } catch (Exception e) {
                fail(key + " does NOT exist");
            }
        });
    }

    @Test
    public void shouldGetProperBrowserOptionsValuesBeNull() {
        Map<String, Object> browserOptionsValues = RuntimeParametersSelenium.BROWSER.getValues();

        assertThat(browserOptionsValues, is(nullValue()));
    }

    @Test
    public void shouldToStringReturnKeyValue() {
        String toStringValue = RuntimeParametersSelenium.BROWSER.toString();

        assertThat(toStringValue, is(equalTo("browser=" + STARTUP_PARAMETERS_VALUES.get("browser"))));
    }
}