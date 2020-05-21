package com.capgemini.mrchecker.test.core.unit.exceptions;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.exceptions.BFWaitingTimeoutException;

public class BFWaitingTimeoutExceptionTest {
	public static final String	COMPONENT_NAME		= "COMPONENT_NAME";
	public static final int		TIMEOUT				= 10;
	public static final String	FORMATTED_MESSAGE	= "Timed out waiting [" + TIMEOUT + "] seconds for [" + COMPONENT_NAME + "] to load.";
	
	@Test
	public void shouldCreateInstance() {
		assertThat(new BFWaitingTimeoutException(COMPONENT_NAME, TIMEOUT), is(notNullValue()));
	}
	
	@Test
	public void shouldFormatMessage() {
		assertThat(new BFWaitingTimeoutException(COMPONENT_NAME, TIMEOUT).getMessage(), is(equalTo(FORMATTED_MESSAGE)));
	}
}
