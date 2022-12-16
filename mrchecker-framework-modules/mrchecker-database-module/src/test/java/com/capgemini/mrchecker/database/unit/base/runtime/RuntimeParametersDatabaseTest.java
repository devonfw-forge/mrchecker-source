package com.capgemini.mrchecker.database.unit.base.runtime;

import com.capgemini.mrchecker.database.core.base.runtime.RuntimeParametersDatabase;
import com.capgemini.mrchecker.database.tags.UnitTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@UnitTest
public class RuntimeParametersDatabaseTest {

    private static final String JPA_PROVIDER_KEY = "provider";
    private static final String JPA_PROVIDER_DEFAULT_VALUE = RuntimeParametersDatabase.JPA_PROVIDER.getValue();
    private static final String JPA_PROVIDER_NEW_VALUE = "NEW_PROVIDER";
    private static final String STRING_EMPTY = "";
    private static final String STRING_NULL_STRING = "null";
    private static final String TO_STRING_VALUE = "provider=hibernate";

    private final static RuntimeParametersDatabase SUT = RuntimeParametersDatabase.JPA_PROVIDER;

    @BeforeEach
    public void setUp() {
        cleanUpSystemProperties();
    }

    @AfterAll
    public static void tearDownClass() {
        cleanUpSystemProperties();
    }

    private static void cleanUpSystemProperties() {
        System.clearProperty(JPA_PROVIDER_KEY);
        SUT.refreshParameterValue();
    }

    @Test
    public void shouldGetKeyReturnJpaProviderKey() {
        assertThat(SUT.getKey(), is(equalTo(JPA_PROVIDER_KEY)));
    }

    @Test
    public void shouldJpaProviderGetValueInitiallyBeDefaultValue() {
        assertThat(SUT.getValue(), is(equalTo(JPA_PROVIDER_DEFAULT_VALUE)));
    }

    @Test
    public void shouldJpaProviderGetValueReturnNewValueWhenSystemPropertyIsSetAndValueRefreshed() {
        setJpaProviderSystemProperty(JPA_PROVIDER_NEW_VALUE);
        SUT.refreshParameterValue();

        assertThat(SUT.getValue(), is(equalTo(JPA_PROVIDER_NEW_VALUE)));
    }

    @ParameterizedTest
    @ValueSource(strings = {STRING_EMPTY, STRING_NULL_STRING})
    public void shouldJpaProviderGetValueReturnDefaultValueWhenSystemPropertyIsSetToInvalidValueAndValueRefreshed(String invalidValue) {
        setJpaProviderSystemProperty(invalidValue);
        SUT.refreshParameterValue();

        assertThat(SUT.getValue(), is(equalTo(JPA_PROVIDER_DEFAULT_VALUE)));
    }

    @Test
    public void shouldJpaProviderGetValueReturnDefaultValueWhenSystemPropertyIsNOTSetAndValueRefreshed() {
        SUT.refreshParameterValue();

        assertThat(SUT.getValue(), is(equalTo(JPA_PROVIDER_DEFAULT_VALUE)));
    }

    @Test
    public void shouldJpaProviderToStringReturnValue() {
        assertThat(SUT.toString(), is(equalTo(TO_STRING_VALUE)));
    }

    private static void setJpaProviderSystemProperty(String value) {
        System.setProperty(JPA_PROVIDER_KEY, value);
    }
}
