package com.capgemini.mrchecker.mobile.core.base.runtime;

import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang.BooleanUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;

public class RuntimeParametersTest {

	private Map<String, String> values = new HashMap<String, String>();

	@Rule
	public ExpectedException exceptionGrabber = ExpectedException.none();

	@Before
	public void setUp() throws Exception {

		values.put("deviceUrl", "http://192.168.0.1:1234");
		values.put("automationName", "magicAutomationName");
		values.put("platformName", "magic_Android");
		values.put("platformVersion", "11.0");
		values.put("deviceName", "magicAndroidEmulator");
		values.put("app", "./magic/path");
		values.put("browserName", "magicChrome");
		values.put("newCommandTimeout", "4");
		values.put("deviceOptions", "headless;window-size=1200x600;testEquals=FirstEquals=SecondEquals;--testMe;acceptInsecureCerts=true;maxInstances=3");

		values.forEach(System::setProperty);

		RuntimeParameters.DEVICE_URL.refreshParameterValue();
		RuntimeParameters.AUTOMATION_NAME.refreshParameterValue();
		RuntimeParameters.PLATFORM_NAME.refreshParameterValue();
		RuntimeParameters.PLATFORM_VERSION.refreshParameterValue();
		RuntimeParameters.DEVICE_NAME.refreshParameterValue();
		RuntimeParameters.APP.refreshParameterValue();
		RuntimeParameters.BROWSER_NAME.refreshParameterValue();
		RuntimeParameters.NEW_COMMAND_TIMEOUT.refreshParameterValue();;
		RuntimeParameters.DEVICE_OPTIONS.refreshParameterValue();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProperty() {

		values.put("deviceUrl", "http://192.168.0.1:1234");
		values.put("automationName", "magicAutomationName");
		values.put("platformName", "magic_Android");
		values.put("platformVersion", "11.0");
		values.put("deviceName", "magicAndroidEmulator");
		values.put("app", "./magic/path");
		values.put("browserName", "magicChrome");
		values.put("newCommandTimeout", "8000");

		assertThat("System parameters for empty property 'deviceUrl' should be 'http://192.168.0.1:1234'", RuntimeParameters.DEVICE_URL.getValue(), Matchers.equalTo("http://192.168.0.1:1234"));
		assertThat("System parameters for empty property 'automationName' should be 'magicAutomationName'", RuntimeParameters.AUTOMATION_NAME.getValue(), Matchers.equalTo("magicAutomationName"));
		assertThat("System parameters for empty property 'platformName' should be 'magic_Android'", RuntimeParameters.PLATFORM_NAME.getValue(), Matchers.equalTo("magic_Android"));
		assertThat("System parameters for empty property 'platformVersion' should be '11.0'", RuntimeParameters.PLATFORM_VERSION.getValue(), Matchers.equalTo("11.0"));
		assertThat("System parameters for empty property 'deviceName' should be 'magicAndroidEmulator'", RuntimeParameters.DEVICE_NAME.getValue(), Matchers.equalTo("magicAndroidEmulator"));
		assertThat("System parameters for empty property 'app' should be './magic/path'", RuntimeParameters.APP.getValue(), Matchers.equalTo("./magic/path"));
		assertThat("System parameters for empty property 'browserName' should be 'magicChrome'", RuntimeParameters.BROWSER_NAME.getValue(), Matchers.equalTo("magicChrome"));
		assertThat("System parameters for empty property 'newCommandTimeout' should be '4'", RuntimeParameters.NEW_COMMAND_TIMEOUT.getValue(), Matchers.equalTo("4"));
		assertThat(
				"System parameters for empty property 'browserOptions' should be 'headless;window-size=1200x600;testEquals=FirstEquals=SecondEquals;--testMe;acceptInsecureCerts=true;maxInstances=3'",
				RuntimeParameters.DEVICE_OPTIONS.getValue(),
				Matchers.equalTo("headless;window-size=1200x600;testEquals=FirstEquals=SecondEquals;--testMe;acceptInsecureCerts=true;maxInstances=3"));

	}

	@Test
	public void testConvertToCorrectTypeBoolean() throws Exception {

		// validate type as Boolean
		String value = "true";
		Object convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		assertThat(BooleanUtils.toBooleanObject((boolean) convertToCorrectType), IsInstanceOf.instanceOf(Boolean.class));

		value = "false";
		convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		assertThat(BooleanUtils.toBooleanObject((boolean) convertToCorrectType), IsInstanceOf.instanceOf(Boolean.class));

		value = "blue";
		convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		exceptionGrabber.expect(ClassCastException.class);
		exceptionGrabber.expectMessage("java.lang.String cannot be cast to java.lang.Boolean");
		assertThat(BooleanUtils.toBooleanObject((boolean) convertToCorrectType), Matchers.not(IsInstanceOf.instanceOf(Boolean.class)));

	}

	@Test
	public void testConvertToCorrectTypeInteger() throws Exception {

		String value = "1";
		Object convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		assertThat((Integer) convertToCorrectType, IsInstanceOf.instanceOf(Integer.class));

		value = "0.23";
		convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		assertThat((Float) convertToCorrectType, IsInstanceOf.instanceOf(Float.class));

		value = "blue";
		convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		exceptionGrabber.expect(ClassCastException.class);
		exceptionGrabber.expectMessage("java.lang.String cannot be cast to java.lang.Integer");
		assertThat((Integer) convertToCorrectType, IsInstanceOf.instanceOf(Integer.class));
	}

	@Test
	public void testConvertToCorrectTypeString() throws Exception {

		String value = "hello";
		Object convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		assertThat((String) convertToCorrectType, IsInstanceOf.instanceOf(String.class));

		value = "";
		convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		assertThat((String) convertToCorrectType, IsInstanceOf.instanceOf(String.class));

		value = null;
		convertToCorrectType = RuntimeParameters.convertToCorrectType(value);
		assertThat((String) convertToCorrectType, Matchers.isEmptyOrNullString());

	}

	@Test
	public void testBrowserOptionsVariable() throws Exception {

		Map<String, Object> expected = new HashMap();
		expected.put("testEquals", "FirstEquals=SecondEquals");
		expected.put("headless", "");
		expected.put("--testMe", "");
		expected.put("window-size", "1200x600");
		expected.put("acceptInsecureCerts", true);
		expected.put("maxInstances", 3);

		assertThat(RuntimeParameters.DEVICE_OPTIONS.getValues()
						.size(),
				Matchers.is(6));

		assertThat(RuntimeParameters.DEVICE_OPTIONS.getValues()
						.toString(),
				Matchers.is(expected.toString()));

	}

	@Test
	public void testBrowserOptionsSet() throws Exception {

		RuntimeParameters.DEVICE_OPTIONS.getValues()
				.forEach((key, value) -> {
					String item = (value.toString()
							.isEmpty()) ? key : key + "=" + value;
					System.out.println(item);
				});

	}

	@Test
	public void testPlatformNameIOS() throws Exception {
		System.setProperty("platformName", "ios");
		RuntimeParameters.PLATFORM_NAME.refreshParameterValue();

		assertEquals("System parameters for empty property 'platformName' should be 'iOS'", "iOS", RuntimeParameters.PLATFORM_NAME.getValue());

	}

	@Test
	public void testGetEmptyProperty() {
		values.forEach((String key, String value) -> System.clearProperty(key));

		RuntimeParameters.DEVICE_URL.refreshParameterValue();
		RuntimeParameters.AUTOMATION_NAME.refreshParameterValue();
		RuntimeParameters.PLATFORM_NAME.refreshParameterValue();
		RuntimeParameters.PLATFORM_VERSION.refreshParameterValue();
		RuntimeParameters.DEVICE_NAME.refreshParameterValue();
		RuntimeParameters.APP.refreshParameterValue();
		RuntimeParameters.BROWSER_NAME.refreshParameterValue();
		RuntimeParameters.NEW_COMMAND_TIMEOUT.refreshParameterValue();
		RuntimeParameters.DEVICE_OPTIONS.refreshParameterValue();

		assertThat("System parameters for empty property 'deviceUrl' should be 'http://127.0.0.1:4723'", RuntimeParameters.DEVICE_URL.getValue(), Matchers.equalTo("http://127.0.0.1:4723"));
		assertThat("System parameters for empty property 'automationName' should be 'Appium'", RuntimeParameters.AUTOMATION_NAME.getValue(), Matchers.equalTo("Appium"));
		assertThat("System parameters for empty property 'platformName' should be 'Android'", RuntimeParameters.PLATFORM_NAME.getValue(), Matchers.equalTo("Android"));
		assertThat("System parameters for empty property 'platformVersion' should be 'null'", RuntimeParameters.PLATFORM_VERSION.getValue(), Matchers.isEmptyString());
		assertThat("System parameters for empty property 'deviceName' should be 'Android Emulator'", RuntimeParameters.DEVICE_NAME.getValue(), Matchers.equalTo("Android Emulator"));
		assertThat("System parameters for empty property 'app' should be '.'", RuntimeParameters.APP.getValue(), Matchers.equalTo("."));
		assertThat("System parameters for empty property 'browserName' should be 'null'", RuntimeParameters.BROWSER_NAME.getValue(), Matchers.isEmptyString());
		assertThat("System parameters for empty property 'newCommandTimeout' should be '4000'", RuntimeParameters.NEW_COMMAND_TIMEOUT.getValue(), Matchers.equalTo("4000"));
		assertThat("System parameters for empty property 'deviceOptions' should be 'null'", RuntimeParameters.DEVICE_OPTIONS.getValue(), Matchers.isEmptyString());

	}

	@Test
	public void testParamsToString() throws Exception {

		RuntimeParameters.valueOf("PLATFORM_NAME")
				.toString()
				.equals("platformName=Android");

	}

}
