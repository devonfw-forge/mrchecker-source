package com.capgemini.mrchecker.cli.core.unit;


import com.capgemini.mrchecker.cli.core.exceptions.BFCliException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class BFCliExceptionTest {

    @Test
    public void shouldCreateInstanceWithNoArgConstructor() {
        assertThat(new BFCliException(), is(notNullValue()));
    }

    @Test
    public void shouldCreateInstanceWithMessage() {
        assertThat(new BFCliException("Message"), is(notNullValue()));
    }

    @Test
    public void shouldCreateInstanceWithThrowableAndMessage() {
        assertThat(new BFCliException("Message", new RuntimeException()), is(notNullValue()));
    }

    @Test
    public void shouldCreateInstanceWithThrowable() {
        assertThat(new BFCliException(new RuntimeException()), is(notNullValue()));
    }

}
