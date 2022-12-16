package com.capgemini.mrchecker.test.core.unit.exceptions;

import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@UnitTest
public class BFInputDataExceptionTest {
    public static final String TEST_MESSAGE = "TEST_MESSAGE";

    @Test
    public void shouldCreateInstance() {
        assertThat(new BFInputDataException(TEST_MESSAGE), is(notNullValue()));
    }

    @Test
    public void shouldNotFormatMessage() {
        assertThat(new BFInputDataException(TEST_MESSAGE).getMessage(), is(equalTo(TEST_MESSAGE)));
    }
}
