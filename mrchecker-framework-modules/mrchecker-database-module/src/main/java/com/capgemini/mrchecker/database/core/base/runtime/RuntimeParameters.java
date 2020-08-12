package com.capgemini.mrchecker.database.core.base.runtime;

import com.capgemini.mrchecker.test.core.base.runtime.IRuntimeParameters;
import org.apache.commons.lang3.StringUtils;

public enum RuntimeParameters implements IRuntimeParameters {
	
	DATABASE_TYPE("databaseType", ""),
	JPA_PROVIDER("provider", "hibernate"),
	DATABASE_USERNAME("databaseUsername", "");
	
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
		
		switch (this.name()) {
		}
		
		this.paramValue = paramValue;
	}
	
	private boolean isSystemParameterEmpty(String systemParameterValue) {
		return (StringUtils.isEmpty(systemParameterValue) || "null".equals(systemParameterValue));
	}
	
}
