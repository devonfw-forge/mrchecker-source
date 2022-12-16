package com.capgemini.mrchecker.database.core.base.runtime;

import com.capgemini.mrchecker.test.core.base.runtime.IRuntimeParameters;
import org.apache.commons.lang3.StringUtils;

public enum RuntimeParametersDatabase implements IRuntimeParameters {

    DATABASE_TYPE("databaseType", ""),
    JPA_PROVIDER("provider", "hibernate");

    private final String paramName;
    private String paramValue;
    private final String defaultValue;

    RuntimeParametersDatabase(String paramName, String defaultValue) {
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

    protected void setValue() {
        String paramValueFromSystem = System.getProperty(getKey());
        this.paramValue = isSystemParameterEmpty(paramValueFromSystem) ? defaultValue : paramValueFromSystem;
    }

    private boolean isSystemParameterEmpty(String systemParameterValue) {
        return (StringUtils.isEmpty(systemParameterValue) || "null".equals(systemParameterValue));
    }
}
