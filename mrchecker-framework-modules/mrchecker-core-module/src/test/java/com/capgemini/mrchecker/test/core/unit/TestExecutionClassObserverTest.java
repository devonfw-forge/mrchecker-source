package com.capgemini.mrchecker.test.core.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import com.capgemini.mrchecker.test.core.utils.FileUtils;

public class TestExecutionClassObserverTest extends TestExecutionObserverBaseTest {
	
	@BeforeAll
	public static void setUpClass() {
		SUT.addObserver(observerMock);
	}
	
	@BeforeEach
	public void setUp() {
		clearInvocations(observerMock);
	}
	
	@AfterAll
	public static void tearDown() {
		SUT.afterAll(contextMock);
	}
	
	@Test
	public void shouldGetInstance() {
		assertThat(TestExecutionObserver.getInstance(), is(notNullValue()));
	}
	
	@Test
	public void shouldGetInstanceTwice() {
		TestExecutionObserver secondRef = TestExecutionObserver.getInstance();
		
		assertThat(secondRef, is(equalTo(SUT)));
	}
	
	@Test
	public void shouldCallBeforeAll() throws IOException {
		SUT.beforeAll(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- CLASS STARTED."));
	}
	
	@Test
	public void shouldCallBeforeTestExecution() throws IOException {
		SUT.beforeTestExecution(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- STARTED."));
	}
	
	@Test
	public void shouldCallAfterTestExecution() throws IOException {
		SUT.afterTestExecution(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- FINISHED."));
	}
	
	@Test
	public void shouldCallTestDisabled() throws IOException {
		SUT.testDisabled(contextMock, Optional.of("Test_reason"));
		
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- DISABLED."));
	}
	
	@Test
	public void shouldCallTestAborted() throws IOException {
		SUT.testAborted(contextMock, new RuntimeException("Test_Exception"));
		
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- ABORTED."));
		verify(observerMock, times(0)).onTestFinish();
	}
	
	@Test
	public void shouldCallTestSuccessful() throws IOException {
		SUT.testSuccessful(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- PASSED."));
		verify(observerMock, times(1)).onTestSuccess();
		verify(observerMock, times(0)).onTestFinish();
	}
	
	@Test
	public void shouldCallTestFailed() throws IOException {
		SUT.testFailed(contextMock, new RuntimeException("Test_Exception"));
		
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- FAILED."));
		verify(observerMock, times(1)).onTestFailure();
		verify(observerMock, times(0)).onTestFinish();
	}
	
	@Test
	public void shouldCallHandleBeforeAllMethodExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleBeforeAllMethodExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- EXCEPTION in @BeforeAll:"));
	}
	
	@Test
	public void shouldCallHandleBeforeEachMethodExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleBeforeEachMethodExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- EXCEPTION in @BeforeEach:"));
	}
	
	@Test
	public void shouldCallHandleTestExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleTestExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- EXCEPTION in @Test:"));
	}
	
	@Test
	public void shouldCallHandleAfterEachMethodExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleAfterEachMethodExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- EXCEPTION in @AfterEach:"));
	}
	
	@Test
	public void shouldCallHandleAfterAllMethodExecutionException() throws Throwable {
		if (TestExecutionObserver.DONT_CONSUME_EXCEPTION_IN_AFTERALL) {
			assertThrows(RuntimeException.class, () -> SUT.handleAfterAllMethodExecutionException(contextMock, new RuntimeException()));
		} else {
			SUT.handleAfterAllMethodExecutionException(contextMock, new RuntimeException());
		}
		assertThat(FileUtils.getAllLinesInFile(FileUtils.getLogFilePath()), containsString("- EXCEPTION in @AfterAll:"));
	}
	
	@Test
	public void shouldAddObserverTwiceHaveNoEffect() throws IOException {
		SUT.addObserver(observerMock);
		
		shouldCallTestSuccessful();
	}
}
