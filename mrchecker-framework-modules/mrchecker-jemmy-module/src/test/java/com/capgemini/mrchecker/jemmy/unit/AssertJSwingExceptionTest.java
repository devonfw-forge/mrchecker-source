package com.capgemini.mrchecker.jemmy.unit;

import com.capgemini.mrchecker.jemmy.exceptions.JemmyException;
import com.capgemini.mrchecker.jemmy.tags.UnitTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

@UnitTest
public class AssertJSwingExceptionTest {

	@Test
	public void shouldCreateInstanceWithNoArgConstructor() {
		assertThat(new JemmyException(), is(notNullValue()));
	}

	@Test
	public void shouldCreateInstanceWithMessage() {
		assertThat(new JemmyException("Message"), is(notNullValue()));
	}

	@Test
	public void shouldCreateInstanceWithThrowableAndMessage() {
		assertThat(new JemmyException("Message", new RuntimeException()), is(notNullValue()));
	}

	@Test
	public void shouldCreateInstanceWithThrowable() {
		assertThat(new JemmyException(new RuntimeException()), is(notNullValue()));
	}

}
