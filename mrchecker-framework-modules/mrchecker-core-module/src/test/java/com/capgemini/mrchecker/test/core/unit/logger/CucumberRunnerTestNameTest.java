package com.capgemini.mrchecker.test.core.unit.logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.logger.CucumberRunnerTestName;
import com.capgemini.mrchecker.test.core.logger.ITestName;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
public class CucumberRunnerTestNameTest {
	
	private static final String TEST_NAME = "Cucumber scenario: 1";
	
	private static final ITestName SUT = CucumberRunnerTestName.parseString(TEST_NAME);
	
	@Test
	public void shouldReturnJunitName() {
		assertThat(SUT.getJunitName(), is(equalTo(TEST_NAME)));
	}
	
	@Test
	public void shouldReturnAllureName() {
		assertThat(SUT.getAllureName(), is(equalTo(TEST_NAME)));
	}
}
