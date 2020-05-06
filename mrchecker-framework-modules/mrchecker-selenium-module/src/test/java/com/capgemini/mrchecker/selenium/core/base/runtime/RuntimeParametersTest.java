package com.capgemini.mrchecker.selenium.core.base.runtime;

import static com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium.INVALID_IE_NAME;
import static com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium.VALID_IE_NAME;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

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
	
	public static final String	DEFAULT_BROWSER_VALUE		= "chrome";
	public static final String	BROWSER_OPTIONS_TEST_KEY	= "browserOptionsTestKey";
	
	@Before
	public void setUp() {
		STARTUP_PARAMETERS_VALUES.forEach(System::setProperty);
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
		doForEachParam((e) -> assertThat(e.toString() + " failed!", e.getValue(), Matchers.equalTo(STARTUP_PARAMETERS_VALUES.get(e.getKey()))));
	}
	
	@Test
	public void shouldGetReturnLowercaseBrowserValue() {
		System.setProperty("browser", STARTUP_PARAMETERS_VALUES.get("browser")
				.toUpperCase());
		
		refreshAllParameters();
		String browserValue = RuntimeParametersSelenium.BROWSER.getValue();
		
		assertThat(browserValue, Matchers.equalTo(STARTUP_PARAMETERS_VALUES.get(RuntimeParametersSelenium.BROWSER.getKey())));
	}
	
	@Test
	public void shouldGetReturnDefaultBrowserValue() {
		Arrays.stream(new String[] { "", "null" })
				.forEach((s) -> {
					System.setProperty("browser", s);
					
					refreshAllParameters();
					String browserValue = RuntimeParametersSelenium.BROWSER.getValue();
					
					assertThat("Failed for '" + s + "'!", browserValue, Matchers.equalTo(DEFAULT_BROWSER_VALUE));
				});
	}
	
	@Test
	public void shouldGetReturnDefaultBrowserValueWhenNoSystemProperty() {
		System.clearProperty("browser");
		
		refreshAllParameters();
		String browserValue = RuntimeParametersSelenium.BROWSER.getValue();
		
		assertThat(browserValue, Matchers.equalTo(DEFAULT_BROWSER_VALUE));
	}
	
	@Test
	public void shouldGetValidIEName() {
		System.setProperty("browser", INVALID_IE_NAME);
		
		refreshAllParameters();
		String browserValue = RuntimeParametersSelenium.BROWSER.getValue();
		
		assertThat(browserValue, Matchers.equalTo(VALID_IE_NAME));
		
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
		
		assertThat(optionValue, Matchers.instanceOf(clazz));
	}
	
	@Test
	public void shouldGetProperBrowserOptionsValuesSize() {
		final String[] browserOptions = STARTUP_PARAMETERS_VALUES.get(RuntimeParametersSelenium.BROWSER_OPTIONS.getKey())
				.split(";");
		
		int browserOptionsCount = RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
				.size();
		
		assertThat(browserOptionsCount, Matchers.is(browserOptions.length));
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
				
				assertThat(browserOption.endsWith(value.toString()), Matchers.equalTo(true));
			} catch (Exception e) {
				fail(key + " does NOT exist");
			}
		});
	}
	
	@Test
	public void shouldGetProperBrowserOptionsValuesBeNull() {
		Map<String, Object> browserOptionsValues = RuntimeParametersSelenium.BROWSER.getValues();
		
		assertThat(browserOptionsValues, Matchers.equalTo(null));
	}
	
	@Test
	public void shouldToStringReturnKeyValue() {
		String toStringValue = RuntimeParametersSelenium.BROWSER.toString();
		
		assertThat(toStringValue, Matchers.equalTo("browser=" + STARTUP_PARAMETERS_VALUES.get("browser")));
	}
}
