package com.capgemini.mrchecker.test.core.unit.exceptions;

import com.capgemini.mrchecker.test.core.exceptions.BFWaitingTimeoutException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.DurationUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@UnitTest
public class BFWaitingTimeoutExceptionTest {
    public static final String COMPONENT_NAME = "COMPONENT_NAME";
    public static final Duration TIMEOUT = Duration.ofSeconds(10);
    public static final String FORMATTED_MESSAGE = "Timed out waiting [" + DurationUtils.getReadableDuration(TIMEOUT) + "] for [" + COMPONENT_NAME + "] to load.";

    @Test
    public void shouldCreateInstance() {
        assertThat(new BFWaitingTimeoutException(COMPONENT_NAME, TIMEOUT), is(notNullValue()));
    }

    @Test
    public void shouldFormatMessage() {
        assertThat(new BFWaitingTimeoutException(COMPONENT_NAME, TIMEOUT).getMessage(), is(equalTo(FORMATTED_MESSAGE)));
    }
}
