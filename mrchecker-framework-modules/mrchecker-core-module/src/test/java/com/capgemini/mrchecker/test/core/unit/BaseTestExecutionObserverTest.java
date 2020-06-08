package com.capgemini.mrchecker.test.core.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.BaseTestExecutionObserver;
import com.capgemini.mrchecker.test.core.ITestObserver;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.FileUtils;

@Tag("one")
@UnitTest
@ResourceLock(value = "BaseTestExecutionObserver")
public class BaseTestExecutionObserverTest {
	private static final BaseTestExecutionObserver	SUT	= BaseTestExecutionObserver.getInstance();
	private static final ExtensionContext			contextMock;
	private static final ITestObserver				observerMock;
	
	public static String logFilePath = FileUtils.getLogFilePath();
	
	static {
		contextMock = mock(ExtensionContext.class);
		when(contextMock.getTestClass()).thenReturn(Optional.of(BaseTestExecutionObserverTest.class));
		when(contextMock.getRequiredTestClass()).thenCallRealMethod();
		when(contextMock.getDisplayName()).thenReturn("Test_name");
		
		observerMock = mock(ITestObserver.class);
		when(observerMock.getModuleType()).thenReturn(ModuleType.CORE);
	}
	
	@BeforeEach
	public void setUp() {
		SUT.addObserver(observerMock);
		clearInvocations(observerMock);
	}
	
	@AfterEach
	public void tearDown() {
		SUT.afterAll(contextMock);
	}
	
	@Test
	public void shouldGetInstance() {
		assertThat(BaseTestExecutionObserver.getInstance(), is(notNullValue()));
	}
	
	@Test
	public void shouldGetInstanceTwice() {
		BaseTestExecutionObserver secondRef = BaseTestExecutionObserver.getInstance();
		
		assertThat(secondRef, is(equalTo(SUT)));
	}
	
	@Test
	public void shouldCallBeforeAll() throws IOException {
		SUT.beforeAll(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- CLASS STARTED."));
	}
	
	@Test
	public void shouldCallBeforeTestExecution() throws IOException {
		SUT.beforeTestExecution(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- STARTED."));
	}
	
	@Test
	public void shouldCallAfterTestExecution() throws IOException {
		SUT.afterTestExecution(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- FINISHED."));
	}
	
	@Test
	public void shouldCallTestDisabled() throws IOException {
		SUT.testDisabled(contextMock, Optional.of("Test_reason"));
		
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- DISABLED."));
	}
	
	@Test
	public void shouldCallTestAborted() throws IOException {
		SUT.testAborted(contextMock, new RuntimeException("Test_Exception"));
		
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- ABORTED."));
		verify(observerMock, times(1)).onTestFinish();
	}
	
	@Test
	public void shouldCallTestSuccessful() throws IOException {
		SUT.testSuccessful(contextMock);
		
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- PASSED."));
		verify(observerMock, times(1)).onTestSuccess();
		verify(observerMock, times(1)).onTestFinish();
	}
	
	@Test
	public void shouldCallTestFailed() throws IOException {
		SUT.testFailed(contextMock, new RuntimeException("Test_Exception"));
		
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- FAILED."));
		verify(observerMock, times(1)).onTestFailure();
		verify(observerMock, times(1)).onTestFinish();
	}
	
	@Test
	public void shouldCallAfterAll() throws IOException {
		SUT.afterAll(contextMock);
		
		verify(observerMock, times(1)).onTestClassFinish();
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("All observers cleared."));
	}
	
	@Test
	public void shouldCallHandleBeforeAllMethodExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleBeforeAllMethodExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- EXCEPTION in @BeforeAll:"));
	}
	
	@Test
	public void shouldCallHandleBeforeEachMethodExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleBeforeEachMethodExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- EXCEPTION in @BeforeEach:"));
	}
	
	@Test
	public void shouldCallHandleTestExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleTestExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- EXCEPTION in @Test:"));
	}
	
	@Test
	public void shouldCallHandleAfterEachMethodExecutionException() throws Throwable {
		assertThrows(RuntimeException.class, () -> SUT.handleAfterEachMethodExecutionException(contextMock, new RuntimeException()));
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- EXCEPTION in @AfterEach:"));
	}
	
	@Test
	public void shouldCallHandleAfterAllMethodExecutionException() throws Throwable {
		if (BaseTestExecutionObserver.DONT_CONSUME_EXCEPTION_IN_AFTERALL) {
			assertThrows(RuntimeException.class, () -> SUT.handleAfterAllMethodExecutionException(contextMock, new RuntimeException()));
		} else {
			SUT.handleAfterAllMethodExecutionException(contextMock, new RuntimeException());
		}
		assertThat(FileUtils.getAllLinesInFile(logFilePath), containsString("- EXCEPTION in @AfterAll:"));
	}
	
	@Test
	public void shouldClearAndAddObserver() {
		SUT.afterAll(contextMock);
		
		SUT.testSuccessful(contextMock);
		
		verify(observerMock, times(0)).onTestSuccess();
		verify(observerMock, times(0)).onTestFinish();
		
		SUT.addObserver(observerMock);
		
		SUT.testSuccessful(contextMock);
		verify(observerMock, times(1)).onTestSuccess();
		verify(observerMock, times(1)).onTestFinish();
	}
	
	@Test
	public void shouldAddObserverTwiceHaveNoEffect() throws IOException {
		SUT.addObserver(observerMock);
		
		shouldCallTestSuccessful();
	}
	
	@Test
	public void shouldRemoveObserverTwiceHaveNoEffect() throws IOException {
		SUT.removeObserver(observerMock);
		
		shouldCallTestSuccessful();
	}
}
