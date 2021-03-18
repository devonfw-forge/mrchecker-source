package com.capgemini.mrchecker.cli.core.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.cli.core.exceptions.BFCliException;
import com.capgemini.mrchecker.cli.core.tags.UnitTest;

@UnitTest
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
