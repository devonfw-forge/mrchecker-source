package com.capgemini.mrchecker.test.core.integration.logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.capgemini.mrchecker.test.core.logger.ITestName;
import com.capgemini.mrchecker.test.core.logger.ITestNameParser;
import com.capgemini.mrchecker.test.core.logger.JunitOrCucumberRunnerTestNameParser;
import com.capgemini.mrchecker.test.core.tags.IntegrationTest;

@IntegrationTest
public class JunitAndCucumberRunnerTestNameParserTest {
	private static final String	JUNIT_RUNNER_DISPLAYED_NAME_NO_DDT	= "Test_orderMenu()";
	private static final String	JUNIT_RUNNER_UNIQUE_ID_NO_DDT		= "[engine:junit-jupiter]/[class:com.capgemini.mrchecker.selenium.mts.MyThaiStarTest]/[method:Test_orderMenu()]";
	
	private static final String	JUNIT_RUNNER_DISPLAYED_NAME_DDT	= "[1] waiter, waiter";
	private static final String	JUNIT_RUNNER_UNIQUE_ID_DDT		= "[engine:junit-jupiter]/[class:com.capgemini.mrchecker.selenium.mts.MyThaiStarTest]/[test-template:Test_loginAndLogOut(com.capgemini.mrchecker.common.mts.data.User)]/[test-template-invocation:#1]";
	private static final String	EXPECTED_ALLURE_NAME_DDT		= "Test_loginAndLogOut: waiter, waiter";
	
	private static final String	CUCUMBER_RUNNER_DISPLAYED_NAME	= "Cucumber test name (scenario line: 1)";
	private static final String	CUCUMBER_RUNNER_UNIQUE_ID_DDT	= "";
	
	private static final ITestNameParser SUT = new JunitOrCucumberRunnerTestNameParser();
	
	@Test
	public void shouldParseJunitRunnerNoDDTName() throws NoSuchMethodException {
		ITestName parsedTestName = SUT.parseFromContext(
				createMockedExtensionContext(JUNIT_RUNNER_DISPLAYED_NAME_NO_DDT, JUNIT_RUNNER_UNIQUE_ID_NO_DDT, Object.class, getClass().getMethod("shouldParseJunitRunnerNoDDTName")));
		
		assertThat(parsedTestName.getJunitName(), is(equalTo("com.capgemini.mrchecker.selenium.mts.MyThaiStarTest:Test_orderMenu")));
		assertThat(parsedTestName.getAllureName(), is(equalTo("Test_orderMenu")));
	}
	
	@Test
	@DisplayName("shouldParseJunitRunnerNoDDTAnnotatedName")
	public void shouldParseJunitRunnerNoDDTAnnotatedName() throws NoSuchMethodException {
		ITestName parsedTestName = SUT.parseFromContext(
				createMockedExtensionContext(JUNIT_RUNNER_DISPLAYED_NAME_NO_DDT, JUNIT_RUNNER_UNIQUE_ID_NO_DDT, Object.class, getClass().getMethod("shouldParseJunitRunnerNoDDTAnnotatedName")));
		
		assertThat(parsedTestName.getJunitName(), is(equalTo("com.capgemini.mrchecker.selenium.mts.MyThaiStarTest:shouldParseJunitRunnerNoDDTAnnotatedName")));
		assertThat(parsedTestName.getAllureName(), is(equalTo("shouldParseJunitRunnerNoDDTAnnotatedName")));
	}
	
	@Test
	public void shouldParseJunitRunnerDDTName() throws NoSuchMethodException {
		ITestName parsedTestName = SUT
				.parseFromContext(createMockedExtensionContext(JUNIT_RUNNER_DISPLAYED_NAME_DDT, JUNIT_RUNNER_UNIQUE_ID_DDT, Object.class, getClass().getMethod("shouldParseJunitRunnerDDTName")));
		
		assertThat(parsedTestName.getJunitName(), is(equalTo("com.capgemini.mrchecker.selenium.mts.MyThaiStarTest" + ":" + EXPECTED_ALLURE_NAME_DDT)));
		assertThat(parsedTestName.getAllureName(), is(equalTo(EXPECTED_ALLURE_NAME_DDT)));
	}
	
	@Test
	@DisplayName("shouldParseJunitRunnerDDTAnnotatedName")
	public void shouldParseJunitRunnerDDTAnnotatedName() throws NoSuchMethodException {
		ITestName parsedTestName = SUT
				.parseFromContext(
						createMockedExtensionContext(JUNIT_RUNNER_DISPLAYED_NAME_DDT, JUNIT_RUNNER_UNIQUE_ID_DDT, Object.class, getClass().getMethod("shouldParseJunitRunnerDDTAnnotatedName")));
		
		assertThat(parsedTestName.getJunitName(), is(equalTo("com.capgemini.mrchecker.selenium.mts.MyThaiStarTest:shouldParseJunitRunnerDDTAnnotatedName: waiter, waiter")));
		assertThat(parsedTestName.getAllureName(), is(equalTo("shouldParseJunitRunnerDDTAnnotatedName: waiter, waiter")));
	}
	
	@Test
	public void shouldParseCucumberRunnerName() throws NoSuchMethodException {
		ITestName parsedTestName = SUT
				.parseFromContext(
						createMockedExtensionContext(CUCUMBER_RUNNER_DISPLAYED_NAME, CUCUMBER_RUNNER_UNIQUE_ID_DDT, BaseHookTest.class, getClass().getMethod("shouldParseCucumberRunnerName")));
		
		assertThat(parsedTestName.getJunitName(), is(equalTo(CUCUMBER_RUNNER_DISPLAYED_NAME)));
		assertThat(parsedTestName.getAllureName(), is(equalTo(CUCUMBER_RUNNER_DISPLAYED_NAME)));
	}
	
	private static ExtensionContext createMockedExtensionContext(String displayedTestName, String uniqueId, Class testInstanceClass, Method testMethod) {
		ExtensionContext contextMock = mock(ExtensionContext.class);
		when(contextMock.getDisplayName()).thenReturn(displayedTestName);
		when(contextMock.getUniqueId()).thenReturn(uniqueId);
		when(contextMock.getRequiredTestClass()).thenReturn(testInstanceClass);
		when(contextMock.getRequiredTestMethod()).thenReturn(testMethod);
		return contextMock;
	}
	
	private static class BaseHookTest {
	}
}
