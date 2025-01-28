package com.capgemini.mrchecker.jemmy.base.runtime;

public enum ScreenshotsConfig {
	ALWAYS,
	ON_FAILURE;

	public static ScreenshotsConfig forValue(String value) {
		switch (value.toLowerCase()) {
		case "onfailure":
			return ON_FAILURE;
		case "always":
		default:
			return ALWAYS;
		}
	}
}
