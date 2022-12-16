package com.capgemini.mrchecker.test.core.unit.exceptions;

import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@UnitTest
public class BFSecureModuleExceptionTest {

    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    public static final String FORMATTED_MESSAGE_WHEN_NULL = "";

    @Test
    public void shouldCreateInstanceWithMessageOnly() {
        assertThat(new BFSecureModuleException(TEST_MESSAGE), is(notNullValue()));
    }

    @Test
    public void shouldNotFormatMessageWithMessageOnly() {
        assertThat(new BFSecureModuleException(TEST_MESSAGE).getMessage(), is(equalTo(TEST_MESSAGE)));
    }

    @Test
    public void shouldFormatMessageWhenNullWithMessageOnly() {
        assertThat(new BFSecureModuleException(null).getMessage(), is(equalTo(FORMATTED_MESSAGE_WHEN_NULL)));
    }

    @Test
    public void shouldCreateInstanceFull() {
        assertThat(new BFSecureModuleException(TEST_MESSAGE, new Throwable()), is(notNullValue()));
    }

    @Test
    public void shouldNotFormatMessageFull() {
        assertThat(new BFSecureModuleException(TEST_MESSAGE, new Throwable()).getMessage(), is(equalTo(TEST_MESSAGE)));
    }

    @Test
    public void shouldFormatMessageWhenNullFull() {
        assertThat(new BFSecureModuleException(null, new Throwable()).getMessage(), is(equalTo(FORMATTED_MESSAGE_WHEN_NULL)));
    }
}
