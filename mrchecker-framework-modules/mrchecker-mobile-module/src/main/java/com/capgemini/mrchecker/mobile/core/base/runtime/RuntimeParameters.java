package com.capgemini.mrchecker.mobile.core.base.runtime;

import com.capgemini.mrchecker.test.core.base.runtime.RuntimeParametersI;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class stores various system properties
 *
 * @author LUSTEFAN
 */
public enum RuntimeParameters implements RuntimeParametersI {

	//http://appium.io/docs/en/writing-running-appium/caps/#appium-desired-capabilities
	//	platformName": "iOS",
	//			"platformVersion": "11.0",
	//			"deviceName": "iPhone 7",
	//			"automationName": "XCUITest",
	//			"app": "/path/to/my.app"

	// GENERIC Capabilities
	// http://appium.io/docs/en/writing-running-appium/caps/#general-capabilities
	DEVICE_URL("deviceUrl", "http://127.0.0.1:4723"),
	AUTOMATION_NAME(MobileCapabilityType.AUTOMATION_NAME, "Appium"),
	PLATFORM_NAME(MobileCapabilityType.PLATFORM_NAME, "Android"),
	PLATFORM_VERSION(MobileCapabilityType.PLATFORM_VERSION, ""),
	DEVICE_NAME(MobileCapabilityType.DEVICE_NAME, "Android Emulator"),
	APP(MobileCapabilityType.APP, "."),
	BROWSER_NAME(MobileCapabilityType.BROWSER_NAME, ""),
	NEW_COMMAND_TIMEOUT(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "4000"),
	DEVICE_OPTIONS("deviceOptions", "") {
		public Map<String, Object> getValues() {
			return Arrays.asList(this.paramValue.split(";"))
					.stream()
					.filter(i -> i != "") // remove empty inputs
					.map(i -> i.split("=", 2)) // split to key, value. Not more than one time
					.map(i -> new String[] { i[0], (i.length == 1) ? "" : i[1] }) // if value is empty, set empty text
					.collect(Collectors.toMap(i -> i[0], i -> (Object) convertToCorrectType(i[1].trim()))); // create
		}

	};

	private String paramName;
	protected String paramValue;
	private String defaultValue;

	private RuntimeParameters(String paramName, String defaultValue) {
		this.paramName = paramName;
		this.defaultValue = defaultValue;
		setValue();

	}

	protected static Object convertToCorrectType(String value) {
		Object convertedValue = value;

		if (null != BooleanUtils.toBooleanObject(value)) {
			convertedValue = Boolean.valueOf(value);
			return convertedValue;
		}

		if (NumberUtils.isNumber(value)) {
			if (NumberUtils.isDigits(value)) {
				// so it is integer value
				convertedValue = Integer.valueOf(value);
				return convertedValue;
			} else {
				// so it is probably float value
				convertedValue = Float.valueOf(value);
				return convertedValue;
			}
		}

		return convertedValue;
	}

	@Override
	public String getValue() {
		return this.paramValue;
	}

	@Override
	public String getKey() {
		return this.paramName;
	}

	public Map<String, Object> getValues() {
		return null;
	}

	@Override
	public String toString() {
		return paramName + "=" + this.getValue();
	}

	@Override
	public void refreshParameterValue() {
		setValue();
	}

	private void setValue() {

		String paramValue = System.getProperty(this.paramName);
		paramValue = isSystemParameterEmpty(paramValue) ? this.defaultValue : paramValue;
		;

		switch (this.name()) {
		//TODO: Set validation for each Runtime Param based on http://appium.io/docs/en/writing-running-appium/caps/#general-capabilities
		//			MobileCapabilityType.PLATFORM_VERSION, "4.4");
		//					MobileCapabilityType.PLATFORM_NAME,"Android");
		//					MobileCapabilityType.AUTOMATION_NAME,"UiAutomator2");
		//					MobileCapabilityType.DEVICE_NAME, "Samsung Galaxy S4 Emulator");
		//					MobileCapabilityType.BROWSER_NAME, "Browser");

		case "AUTOMATION_NAME":
			break;

		case "PLATFORM_VERSION":
			break;
		case "DEVICE_URL":
			break;
		case "DEVICE_OPTIONS":
			break;
		case "PLATFORM_NAME":
			if (paramValue.equals("android")) {
				paramValue = "Android";
			} else if (paramValue.equals("ios")) {
				paramValue = "iOS";
			}
			break;
		case "DEVICE_NAME":
			if (paramValue.equals("android")) {
				paramValue = "Android Emulator";
			}
			break;
		case "BROWSER_NAME":
			break;
		case "NEW_COMMAND_TIMEOUT":
			break;
		case "APP":
			break;
		case "APPLICATION_PACKAGE":
			break;
		case "APPLICATION_ACTIVITY":
			break;
		default:
			BFLogger.logError("Unknown RuntimeParameter = " + this.name());
			break;
		}

		this.paramValue = paramValue;

	}

	private boolean isSystemParameterEmpty(String systemParameterValue) {
		return (null == systemParameterValue || "".equals(systemParameterValue) || "null".equals(systemParameterValue));
	}

}
