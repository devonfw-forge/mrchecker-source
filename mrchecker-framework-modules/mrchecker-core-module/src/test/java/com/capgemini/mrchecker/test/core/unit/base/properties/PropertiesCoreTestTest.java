package com.capgemini.mrchecker.test.core.unit.base.properties;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesCoreTest;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@UnitTest
@ResourceLock(value = "SingleThread")
public class PropertiesCoreTestTest {
    public static final String DEFAULT_ENV = "DEV";
    public static final boolean DEFAULT_ANALYTICS = true;
    public static final boolean DEFAULT_ENCRYPTION = false;

    private static PropertiesCoreTest sut;

    @BeforeAll
    public static void setUpClass() {
        sut = new PropertiesCoreTest();
    }

    @Test
    public void shouldInitiallyBeDefaultEnv() {
        assertThat(sut.getDefaultEnvironmentName(), is(equalTo(DEFAULT_ENV)));
    }

    @Test
    public void shouldInitiallyBeAnalyticsEnabled() {
        assertThat(sut.isAnalyticsEnabled(), is(equalTo(DEFAULT_ANALYTICS)));
    }

    @Test
    public void shouldInitiallyBeEncryptionDisabled() {
        assertThat(sut.isEncryptionEnabled(), is(equalTo(DEFAULT_ENCRYPTION)));
    }
}
