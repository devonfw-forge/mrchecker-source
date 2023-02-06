package com.capgemini.mrchecker.selenium.core.base.runtime;

import com.capgemini.mrchecker.test.core.base.runtime.IRuntimeParameters;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class stores various system properties
 *
 * @author LUSTEFAN
 * @author MBABIARZ
 */
public enum RuntimeParametersSelenium implements IRuntimeParameters {
    BROWSER("browser", "chrome") {
        protected void setValue() {
            super.setValue();
            paramValue = paramValue.toLowerCase();
            if (paramValue.equals(INVALID_IE_NAME))
                paramValue = VALID_IE_NAME;
        }
    },
    BROWSER_VERSION("browserVersion", ""),
    SELENIUM_GRID("seleniumGrid", ""),
    OS("os", ""),
    BROWSER_OPTIONS("browserOptions", "") {
        public Map<String, Object> getValues() {
            return Arrays.stream(getValue().split(BROWSER_OPTIONS_DELIMITER_REGEX))
                    .filter(i -> !i.isEmpty()) // remove
                    // empty
                    // inputs
                    .map(i -> i.split(BROWSER_OPTIONS_KEY_VALUE_DELIMITER_REGEX, TOKENS_LIMIT)) // split to key, value.
                    // Not more than one time
                    .map(i -> new String[]{i[0], (i.length == 1) ? "" : i[1]}) // if value is empty, set empty text
                    .collect(Collectors.toMap(i -> i[0], i -> convertToCorrectType(i[1].trim()))); // create
        }
    };

    public static final String VALID_IE_NAME = "internet explorer";
    public static final String INVALID_IE_NAME = "ie";
    private static final int TOKENS_LIMIT = 2;
    private static final String BROWSER_OPTIONS_KEY_VALUE_DELIMITER_REGEX = "=";
    private static final String BROWSER_OPTIONS_DELIMITER_REGEX = ";";

    private final String paramName;
    private final String defaultValue;
    protected String paramValue;

    RuntimeParametersSelenium(String paramName, String defaultValue) {
        this.paramName = paramName;
        this.defaultValue = defaultValue;
        setValue();
    }

    @Override
    public String getValue() {
        return paramValue;
    }

    public Map<String, Object> getValues() {
        return null;
    }

    @Override
    public String getKey() {
        return paramName;
    }

    @Override
    public void refreshParameterValue() {
        setValue();
    }

    protected void setValue() {
        String paramValueFromSystem = System.getProperty(getKey());
        this.paramValue = isSystemParameterEmpty(paramValueFromSystem) ? defaultValue : paramValueFromSystem;
    }

    private static Object convertToCorrectType(String value) {
        if (!Objects.isNull(BooleanUtils.toBooleanObject(value))) {
            return Boolean.valueOf(value);
        }

        if (NumberUtils.isNumber(value)) {
            if (NumberUtils.isDigits(value)) {
                return Integer.parseInt(value);
            } else {
                return Float.parseFloat(value);
            }
        }

        return value;
    }

    private static boolean isSystemParameterEmpty(String paramValueFromSystem) {
        return StringUtils.isEmpty(paramValueFromSystem) || "null".equals(paramValueFromSystem);
    }

    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}