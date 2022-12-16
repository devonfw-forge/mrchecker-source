package com.capgemini.mrchecker.test.core.unit.base.runtime;

import com.capgemini.mrchecker.test.core.base.runtime.RuntimeParametersCore;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@UnitTest
@ResourceLock(value = "SingleThread")
public class RuntimeParametersCoreTest {

    private static final String ENV_KEY = "env";
    private static final String ENV_DEFAULT_VALUE = RuntimeParametersCore.ENV.getValue();
    private static final String ENV_NEW_DEFAULT_VALUE = "NEW_DEFAULT";
    private static final String ENV_NEW_VALUE = "NEW_ENV";
    private static final String STRING_EMPTY = "";
    private static final String STRING_NULL_STRING = "null";
    private static final String TO_STRING_VALUE = "env=" + System.getProperty("env", "DEV");

    private final static RuntimeParametersCore sut = RuntimeParametersCore.ENV;

    @BeforeEach
    public void setUp() {
        cleanUpSystemProperties();
    }

    @AfterAll
    public static void tearDownClass() {
        cleanUpSystemProperties();
    }

    private static void cleanUpSystemProperties() {
        System.clearProperty(ENV_KEY);
        sut.setDefaultValue(ENV_DEFAULT_VALUE);
        sut.refreshParameterValue();
    }

    @Test
    public void shouldGetKeyReturnEnvKey() {
        assertThat(sut.getKey(), is(equalTo(ENV_KEY)));
    }

    @Test
    public void shouldEnvGetValueInitiallyBeDefaultValue() {
        assertThat(sut.getValue(), is(equalTo(ENV_DEFAULT_VALUE)));
    }

    @Test
    public void shouldEnvGetValueReturnDefaultValueWhenSystemPropertyIsSetAndValueNOTRefreshed() {
        setEnvSystemProperty(ENV_NEW_VALUE);

        assertThat(sut.getValue(), is(equalTo(ENV_DEFAULT_VALUE)));
    }

    @Test
    public void shouldEnvGetValueReturnNewValueWhenSystemPropertyIsSetAndValueRefreshed() {
        setEnvSystemProperty(ENV_NEW_VALUE);
        sut.refreshParameterValue();

        assertThat(sut.getValue(), is(equalTo(ENV_NEW_VALUE)));
    }

    @ParameterizedTest
    @ValueSource(strings = {STRING_EMPTY, STRING_NULL_STRING})
    public void shouldEnvGetValueReturnDefaultValueWhenSystemPropertyIsSetToInvalidValueAndValueRefreshed(String invalidValue) {
        setEnvSystemProperty(invalidValue);
        sut.refreshParameterValue();

        assertThat(sut.getValue(), is(equalTo(ENV_DEFAULT_VALUE)));
    }

    @Test
    public void shouldEnvGetValueReturnDefaultValueWhenSystemPropertyIsNOTSetAndValueRefreshed() {
        sut.refreshParameterValue();

        assertThat(sut.getValue(), is(equalTo(ENV_DEFAULT_VALUE)));
    }

    @Test
    public void shouldEnvSetDefaultValueAndValueRefreshed() {
        sut.setDefaultValue(ENV_NEW_DEFAULT_VALUE);
        sut.refreshParameterValue();

        assertThat(sut.getValue(), is(equalTo(ENV_NEW_DEFAULT_VALUE)));
    }

    @Test
    public void shouldEnvNOTSetDefaultValueAndValueNOTRefreshed() {
        sut.setDefaultValue(ENV_NEW_DEFAULT_VALUE);

        assertThat(sut.getValue(), is(equalTo(ENV_DEFAULT_VALUE)));
    }

    @ParameterizedTest
    @ValueSource(strings = {STRING_EMPTY, STRING_NULL_STRING})
    public void shouldEnvNotSetDefaultValueWhenNewDefaultValueIsInvalidAndValueRefreshed(String invalidValue) {
        sut.setDefaultValue(invalidValue);
        sut.refreshParameterValue();

        assertThat(sut.getValue(), is(equalTo(ENV_DEFAULT_VALUE)));
    }

    @Test
    public void shouldEnvNotSetDefaultValueWhenNewDefaultValueNullAndValueRefreshed() {
        sut.setDefaultValue(null);
        sut.refreshParameterValue();

        assertThat(sut.getValue(), is(equalTo(ENV_DEFAULT_VALUE)));
    }

    @Test
    public void shouldEnvToStringReturnValue() {
        assertThat(sut.toString(), is(equalTo(TO_STRING_VALUE)));
    }

    private static void setEnvSystemProperty(String value) {
        System.setProperty(ENV_KEY, value);
    }
}
