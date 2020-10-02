package com.capgemini.mrchecker.test.core.unit.logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.logger.EmptyTestName;
import com.capgemini.mrchecker.test.core.logger.ITestName;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
public class EmptyTestNameTest {
	private static final ITestName SUT = new EmptyTestName();
	
	@Test
	public void shouldReturnJunitName() {
		assertThat(SUT.getJunitName(), is(equalTo("")));
	}
	
	@Test
	public void shouldReturnAllureName() {
		assertThat(SUT.getAllureName(), is(equalTo("")));
	}
}
