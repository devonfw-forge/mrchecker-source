package com.capgemini.mrchecker.test.core.integration;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ITestObserver;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.tags.IntegrationTest;
import com.capgemini.mrchecker.test.core.utils.TestLauncher;

import io.qameta.allure.Feature;
import io.qameta.allure.Muted;

@IntegrationTest
@ResourceLock(value = "SingleThread")
public class RunningTestsTest {
	public static ITestObserver	testObserver;
	public static ITestObserver	testObserverSecond;
	
	private static boolean	isBaseTestSetupCalled;
	private static boolean	isBaseTestTeardownCalled;
	
	private static final boolean	CALLED		= true;
	private static final boolean	NOT_CALLED	= false;
	
	public static final String CLASS_PREFIX = "com.capgemini.mrchecker.test.core.integration.RunningTestsTest$";
	
	@BeforeEach
	public void setUp() {
		isBaseTestSetupCalled = NOT_CALLED;
		isBaseTestTeardownCalled = NOT_CALLED;
	}
	
	public static class BaseTestWithHooks extends BaseTest {
		@Override
		public void setUp() {
			isBaseTestSetupCalled = CALLED;
		}
		
		@Override
		public void tearDown() {
			isBaseTestTeardownCalled = CALLED;
		}
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/datadriven/integration/running_tests.csv", numLinesToSkip = 1)
	public void shouldRunTestBundle(String className,
			int expectedOnTestSuccessCount,
			int expectedOnTestFailureCount,
			int expectedOnTestFinishCount,
			int expectedOnTestClassFinishCount,
			boolean expectedAreHooksCalled) throws ClassNotFoundException {
		testObserver = mock(ITestObserver.class);
		when(testObserver.getModuleType()).thenReturn(ModuleType.CORE);
		
		TestLauncher.launch(CLASS_PREFIX + className);
		
		verify(testObserver, times(expectedOnTestSuccessCount)).onTestSuccess();
		verify(testObserver, times(expectedOnTestFailureCount)).onTestFailure();
		verify(testObserver, times(expectedOnTestFinishCount)).onTestFinish();
		verify(testObserver, times(expectedOnTestClassFinishCount)).onTestClassFinish();
		
		assertThat(isBaseTestSetupCalled, is(equalTo(expectedAreHooksCalled)));
		assertThat(isBaseTestTeardownCalled, is(equalTo(expectedAreHooksCalled)));
	}
	
	@Feature("Running tests")
	@Muted
	public static class PassingTestWithObserverAddedInTestMethod extends BaseTestWithHooks {
		@Test
		public void simpleTest() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class PassingTestWithObserverAddedTwiceInTestMethod extends BaseTestWithHooks {
		
		@Test
		public void simpleTest() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
	}
	
	@Test
	public void shouldPassTestWithTwoObserversAddedInTestMethod() {
		testObserver = mock(ITestObserver.class);
		when(testObserver.getModuleType()).thenReturn(ModuleType.CORE);
		testObserverSecond = mock(ITestObserver.class);
		when(testObserverSecond.getModuleType()).thenReturn(ModuleType.CORE);
		
		TestLauncher.launch(PassingTestWithTwoObserversAddedInTestMethod.class);
		
		verify(testObserver, times(1)).onTestSuccess();
		verify(testObserver, times(0)).onTestFailure();
		verify(testObserver, times(1)).onTestFinish();
		verify(testObserver, times(1)).onTestClassFinish();
		
		verify(testObserverSecond, times(0)).onTestSuccess();
		verify(testObserverSecond, times(0)).onTestFailure();
		verify(testObserverSecond, times(0)).onTestFinish();
		verify(testObserverSecond, times(0)).onTestClassFinish();
		
		assertThat(isBaseTestSetupCalled, is(equalTo(CALLED)));
		assertThat(isBaseTestTeardownCalled, is(equalTo(CALLED)));
	}
	
	@Feature("Running tests")
	@Muted
	public static class PassingTestWithTwoObserversAddedInTestMethod extends BaseTestWithHooks {
		
		@Test
		public void simpleTest() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserverSecond);
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class PassingTestWithObserverAddedInBeforeAll extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@Test
		public void simpleTest() {
		}
	}
	
	@Test
	public void shouldPassTestsMultithreaded() {
		testObserver = mock(ITestObserver.class);
		testObserverSecond = mock(ITestObserver.class);
		
		List<Class<? extends BaseTest>> testClasses = new ArrayList<>();
		testClasses.add(shouldPassTestsMultithreadedThread1.class);
		testClasses.add(shouldPassTestsMultithreadedThread2.class);
		TestLauncher.launch(testClasses);
		
		verify(testObserver, times(1)).onTestSuccess();
		verify(testObserver, times(0)).onTestFailure();
		verify(testObserver, times(0)).onTestFinish();
		verify(testObserver, times(1)).onTestClassFinish();
		
		verify(testObserverSecond, times(1)).onTestSuccess();
		verify(testObserverSecond, times(0)).onTestFailure();
		verify(testObserverSecond, times(0)).onTestFinish();
		verify(testObserverSecond, times(1)).onTestClassFinish();
		
		assertThat(isBaseTestSetupCalled, is(equalTo(CALLED)));
		assertThat(isBaseTestTeardownCalled, is(equalTo(CALLED)));
	}
	
	@Feature("Running tests")
	@Muted
	public static class shouldPassTestsMultithreadedThread1 extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@Test
		public void simpleTest() {
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class shouldPassTestsMultithreadedThread2 extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserverSecond);
		}
		
		@Test
		public void simpleTest() {
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestDueToFailingAssertion extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@Test
		public void simpleTest() {
			fail();
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestDueToExceptionInTestMethod extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@Test
		public void simpleTest() {
			throw new RuntimeException();
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestDueToExceptionInBeforeAll extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
			throw new RuntimeException();
		}
		
		@Test
		public void simpleTest() {
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestDueToExceptionInBeforeEach extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@BeforeEach
		public void setUp() {
			throw new RuntimeException();
		}
		
		@Test
		public void simpleTest() {
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestThatPassedExecutionDueToExceptionInAfterEach extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@AfterEach
		public void teardown() {
			throw new RuntimeException();
		}
		
		@Test
		public void simpleTest() {
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestThatFailedExecutionDueToExceptionInAfterEach extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@AfterEach
		public void teardown() {
			throw new RuntimeException();
		}
		
		@Test
		public void simpleTest() {
			fail();
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestThatPassedExecutionDueToExceptionInAfterAll extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@AfterAll
		public static void teardown() {
			throw new RuntimeException();
		}
		
		@Test
		public void simpleTest() {
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class FailingTestThatFailedExecutionDueToExceptionInAfterAll extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@AfterAll
		public static void teardown() {
			throw new RuntimeException();
		}
		
		@Test
		public void simpleTest() {
			fail();
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class DisabledTestWithObserverAddedInTestMethod extends BaseTestWithHooks {
		
		@Disabled
		@Test
		public void simpleTest() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class DisabledTestWithObserverAddedInBeforeAll extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@Disabled
		@Test
		public void simpleTest() {
			
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class AbortTestWithObserverAddedInTestMethod extends BaseTestWithHooks {
		
		@Test
		public void simpleTest() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
			assumeFalse(true, "For test purpose");
		}
	}
	
	@Feature("Running tests")
	@Muted
	public static class AbortTestWithObserverAddedInBeforeAll extends BaseTestWithHooks {
		
		@BeforeAll
		public static void setUpClass() {
			BASE_TEST_EXECUTION_OBSERVER.addObserver(testObserver);
		}
		
		@Test
		public void simpleTest() {
			assumeFalse(true, "For test purpose");
		}
	}
	
	private static void printSummary(TestExecutionSummary summary) {
		try (PrintWriter out = new PrintWriter(System.out)) {
			summary.printTo(out);
		}
	}
}
