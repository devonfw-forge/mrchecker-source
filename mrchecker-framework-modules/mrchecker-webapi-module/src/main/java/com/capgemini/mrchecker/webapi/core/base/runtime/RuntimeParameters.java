package com.capgemini.mrchecker.webapi.core.base.runtime;

import com.capgemini.mrchecker.test.core.base.runtime.IRuntimeParameters;
import org.apache.commons.lang.StringUtils;

/**
 * This class stores various system properties These parameters are accessible while test case executes Example :
 * PARAM_3("param_3", "1410") IS: mvn test -Dparam_3=1525 -Dtest=MyTestClass
 *
 * @author LUSTEFAN
 */
public enum RuntimeParameters implements IRuntimeParameters {

    // NAME(<maven-variable-name>, <default-value>)
    MOCK_HTTP_PORT("mock_http_port", ""), // -Dmock_http_port="" Default random
    MOCK_HTTP_HOST("mock_http_host", "http://localhost"); // -Dmock_http_host="" Default localhost

    private final String paramName;
    private String paramValue;
    private final String defaultValue;

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
        String paramValue = System.getProperty(this.paramName);
        paramValue = isSystemParameterEmpty(paramValue) ? this.defaultValue : paramValue.toLowerCase();
        this.paramValue = paramValue;
    }

    private boolean isSystemParameterEmpty(String systemParameterValue) {
        return (StringUtils.isEmpty(systemParameterValue) || "null".equals(systemParameterValue));
    }

}
