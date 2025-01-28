package com.capgemini.mrchecker.jemmy.base.runtime;

import static com.capgemini.mrchecker.jemmy.base.runtime.RuntimeParametersJemmy.SCREENSHOTS;

public enum ScreenshotsConfig {
	ALWAYS,
	ON_FAILURE;

	public static ScreenshotsConfig getConfig() {
		switch (SCREENSHOTS.getValue().toLowerCase()) {
		case "onfailure":
			return ON_FAILURE;
		case "always":
		default:
			return ALWAYS;
		}
	}
}
