package com.capgemini.mrchecker.test.core.unit;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.analytics.AnalyticsProvider;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@UnitTest
@ResourceLock(value = "SingleThread")
public class BaseTestTest {

    @Test
    public void shouldGetAnalyticsReturnDisabled() {
        assertThat(BaseTest.getAnalytics(), is(equalTo(AnalyticsProvider.DISABLED)));
    }

    @Test
    public void shouldGetEnvironmentServiceReturnInstance() {
        assertThat(BaseTest.getEnvironmentService(), is(not(nullValue())));
    }

    @Test
    public void shouldSetEnvironmentServiceReturnInstance() {
        IEnvironmentService envServiceMock = mock(IEnvironmentService.class);

        BaseTest.setEnvironmentService(envServiceMock);

        assertThat(BaseTest.getEnvironmentService(), is(equalTo(envServiceMock)));
    }

    @Test
    public void shouldCallSetUp() {
        new BaseTest() {
        }.setUp();

        assertTrue(true);
    }

    @Test
    public void shouldCallTearDown() {
        new BaseTest() {
        }.tearDown();

        assertTrue(true);
    }
}