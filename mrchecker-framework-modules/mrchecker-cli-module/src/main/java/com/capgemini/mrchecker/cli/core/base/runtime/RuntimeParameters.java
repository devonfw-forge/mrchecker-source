package com.capgemini.mrchecker.cli.core.base.runtime;

import org.apache.commons.lang.StringUtils;

import com.capgemini.mrchecker.test.core.base.runtime.IRuntimeParameters;
import com.capgemini.mrchecker.test.core.logger.BFLogger;

/**
 * This class stores various system properties These parameters are accessible while test case executes Example :
 * PARAM_3("param_3", "1410") IS: mvn test -Dparam_3=1525 -Dtest=MyTestClass
 * 
 * @author LUSTEFAN
 */
public enum RuntimeParameters implements IRuntimeParameters {
	
	// NAME(<maven-variable-name>, <default-value>)
	PARAM_1("param_1", "Hello"), // -Dparam_1=Hello
	PARAM_2("param_2", "world"), // -Dparam_2=world
	PARAM_3("param_3", "1410"); // -Dparam_3=1410
	
	private final String	paramName;
	private String			paramValue;
	private final String	defaultValue;
	
	RuntimeParameters(String paramName, String defaultValue) {
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
		
		String paramValue = System.getProperty(paramName);
		paramValue = isSystemParameterEmpty(paramValue) ? defaultValue : paramValue.toLowerCase();
		
		switch (name()) {
			case "PARAM_1":
				if (paramValue.equals("Bye")) {
					paramValue = "Hi";
				}
				break;
			case "PARAM_2":
			case "PARAM_3":
				break;
			default:
				BFLogger.logError("Unknown RuntimeParameter = " + name());
				break;
		}
		
		this.paramValue = paramValue;
		
	}
	
	private boolean isSystemParameterEmpty(String systemParameterValue) {
		return StringUtils.isEmpty(systemParameterValue) || "null".equals(systemParameterValue);
	}
	
}
