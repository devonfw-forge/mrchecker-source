package com.capgemini.mrchecker.test.core.unit.exceptions;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

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
