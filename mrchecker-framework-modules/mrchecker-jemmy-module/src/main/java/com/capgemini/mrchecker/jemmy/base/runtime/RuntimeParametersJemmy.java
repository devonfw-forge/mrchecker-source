package com.capgemini.mrchecker.jemmy.base.runtime;

import com.capgemini.mrchecker.test.core.base.runtime.IRuntimeParameters;
import org.apache.commons.lang.StringUtils;

/**
 * This class stores various system properties
 *
 * @author LUSTEFAN
 */
public enum RuntimeParametersJemmy implements IRuntimeParameters {

	SCREENSHOTS("screenshots", "always");

	private final String paramName;
	private String paramValue;
	private String defaultValue;

	RuntimeParametersJemmy(String paramName, String defaultValue) {
		this.paramName = paramName;
		this.defaultValue = defaultValue;
		setValue();
	}

	@Override
	public String getValue() {
		return paramValue;
	}

	@Override
	public String getKey() {
		return paramName;
	}

	@Override
	public String toString() {
		return getKey() + "=" + getValue();
	}

	@Override
	public void refreshParameterValue() {
		setValue();
	}

	private void setValue() {
		String systemParameterValue = System.getProperty(paramName);
		paramValue = isSystemParameterEmpty(systemParameterValue) ? defaultValue : systemParameterValue;
	}

	public void setDefaultValue(String value) {
		defaultValue = isSystemParameterEmpty(value) ? defaultValue : value;
	}

	private boolean isSystemParameterEmpty(String systemParameterValue) {
		return (StringUtils.isEmpty(systemParameterValue) || "null".equals(systemParameterValue));
	}
}
