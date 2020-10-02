package com.capgemini.mrchecker.test.core.integration.logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.capgemini.mrchecker.test.core.logger.ITestName;
import com.capgemini.mrchecker.test.core.logger.ITestNameParser;
import com.capgemini.mrchecker.test.core.logger.JunitOrCucumberRunnerTestNameParser;
import com.capgemini.mrchecker.test.core.tags.IntegrationTest;

@IntegrationTest
public class JunitAndCucumberRunnerTestNameParserTest {
	private static final String	JUNIT_RUNNER_DISPLAYED_NAME_NO_TDD	= "Test_orderMenu()";
	private static final String	JUNIT_RUNNER_UNIQUE_ID_NO_TDD		= "[engine:junit-jupiter]/[class:com.capgemini.mrchecker.selenium.mts.MyThaiStarTest]/[method:Test_orderMenu()]";
	
	private static final String	JUNIT_RUNNER_DISPLAYED_NAME_TDD	= "[1] waiter, waiter";
	private static final String	JUNIT_RUNNER_UNIQUE_ID_TDD		= "[engine:junit-jupiter]/[class:com.capgemini.mrchecker.selenium.mts.MyThaiStarTest]/[test-template:Test_loginAndLogOut(com.capgemini.mrchecker.common.mts.data.User)]/[test-template-invocation:#1]";
	
	private static final String	CUCUMBER_RUNNER_DISPLAYED_NAME	= "Cucumber test name (scenario line: 1)";
	private static final String	CUCUMBER_RUNNER_UNIQUE_ID_TDD	= "";
	
	private static final ITestNameParser SUT = new JunitOrCucumberRunnerTestNameParser();
	
	@Test
	public void shouldParseJunitRunnerNoTDDName() {
		ITestName parsedTestName = SUT.parseFromContext(createMockedExtensionContext(JUNIT_RUNNER_DISPLAYED_NAME_NO_TDD, JUNIT_RUNNER_UNIQUE_ID_NO_TDD, Object.class));
		
		assertThat(parsedTestName.getJunitName(), is(equalTo("com.capgemini.mrchecker.selenium.mts.MyThaiStarTest" + ":" + "Test_orderMenu")));
		assertThat(parsedTestName.getAllureName(), is(equalTo("Test_orderMenu")));
	}
	
	@Test
	public void shouldParseJunitRunnerTDDName() {
		ITestName parsedTestName = SUT.parseFromContext(createMockedExtensionContext(JUNIT_RUNNER_DISPLAYED_NAME_TDD, JUNIT_RUNNER_UNIQUE_ID_TDD, Object.class));
		String testNameSuffix = "Test_loginAndLogOut" + ":" + JUNIT_RUNNER_DISPLAYED_NAME_TDD;
		
		assertThat(parsedTestName.getJunitName(), is(equalTo("com.capgemini.mrchecker.selenium.mts.MyThaiStarTest" + ":" + testNameSuffix)));
		assertThat(parsedTestName.getAllureName(), is(equalTo(testNameSuffix)));
	}
	
	@Test
	public void shouldParseCucumberRunnerName() {
		ITestName parsedTestName = SUT.parseFromContext(createMockedExtensionContext(CUCUMBER_RUNNER_DISPLAYED_NAME, CUCUMBER_RUNNER_UNIQUE_ID_TDD, BaseHookTest.class));
		
		assertThat(parsedTestName.getJunitName(), is(equalTo(CUCUMBER_RUNNER_DISPLAYED_NAME)));
		assertThat(parsedTestName.getAllureName(), is(equalTo(CUCUMBER_RUNNER_DISPLAYED_NAME)));
	}
	
	private static ExtensionContext createMockedExtensionContext(String displayedTestName, String uniqueId, Class testInstanceClass) {
		ExtensionContext contextMock = mock(ExtensionContext.class);
		when(contextMock.getDisplayName()).thenReturn(displayedTestName);
		when(contextMock.getUniqueId()).thenReturn(uniqueId);
		when(contextMock.getRequiredTestClass()).thenReturn(testInstanceClass);
		return contextMock;
	}
	
	private static class BaseHookTest {
		
	}
	
}
