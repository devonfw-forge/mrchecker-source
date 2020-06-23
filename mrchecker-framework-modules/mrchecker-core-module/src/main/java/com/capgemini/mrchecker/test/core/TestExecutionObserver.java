package com.capgemini.mrchecker.test.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.Attachments;

public class TestExecutionObserver implements ITestExecutionObserver {
	
	private static TestExecutionObserver instance;
	
	private final ThreadLocal<Long> stopwatch = ThreadLocal.withInitial(() -> 0L);
	
	private final ThreadLocal<List<ITestObserver>>	observers		= ThreadLocal.withInitial(ArrayList::new);
	private final ThreadLocal<List<ITestObserver>>	classObservers	= ThreadLocal.withInitial(ArrayList::new);
	
	public static final boolean DONT_CONSUME_EXCEPTION_IN_AFTERALL = false;
	
	private TestExecutionObserver() {
	}
	
	public static TestExecutionObserver getInstance() {
		if (Objects.isNull(instance)) {
			synchronized (TestExecutionObserver.class) {
				if (Objects.isNull(instance)) {
					instance = new TestExecutionObserver();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void beforeAll(ExtensionContext extensionContext) {
		String testName = extensionContext.getRequiredTestClass()
				.getName();
		BFLogger.logInfo("\"" + testName + "\"" + " - CLASS STARTED.");
	}
	
	@Override
	public void beforeTestExecution(ExtensionContext context) {
		BFLogger.RestrictedMethods.startSeparateLog();
		logTestInfo(context, "STARTED");
		BaseTest.getAnalytics()
				.sendClassName();
		stopwatch.set(System.currentTimeMillis());
		validateTestClassAndCallHook(context, BaseTest::setUp);
	}
	
	@Override
	public void afterTestExecution(ExtensionContext context) {
		stopwatch.set(System.currentTimeMillis() - stopwatch.get()); // end timing
		String testName = logTestInfo(context, "FINISHED");
		printTimeExecutionLog(testName);
		validateTestClassAndCallHook(context, BaseTest::tearDown);
	}
	
	private static void makeLogForTest() {
		Attachments.attachToAllure(BFLogger.RestrictedMethods.dumpSeparateLog());
	}
	
	@Override
	public void testDisabled(ExtensionContext context, Optional<String> reason) {
		logTestInfo(context, "DISABLED");
		reason.ifPresent(s -> BFLogger.logInfo("Reason: " + s));
	}
	
	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {
		logTestInfo(context, "ABORTED");
		afterEach();
	}
	
	@Override
	public void testSuccessful(ExtensionContext context) {
		logTestInfo(context, "PASSED");
		observers.get()
				.forEach(ITestObserver::onTestSuccess);
		classObservers.get()
				.forEach(ITestObserver::onTestSuccess);
		afterEach();
	}
	
	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		logTestInfo(context, "FAILED");
		observers.get()
				.forEach(ITestObserver::onTestFailure);
		classObservers.get()
				.forEach(ITestObserver::onTestFailure);
		afterEach();
	}
	
	private static String logTestInfo(ExtensionContext context, String testStatus) {
		String className = context.getRequiredTestClass()
				.getName();
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + className + "#" + testName + "\"" + " - " + testStatus + ".");
		return testName;
	}
	
	private void afterEach() {
		observers.get()
				.forEach(ITestObserver::onTestFinish);
		makeLogForTest();
	}
	
	private static void validateTestClassAndCallHook(ExtensionContext context, Consumer<BaseTest> hook) {
		Object testClass = context.getRequiredTestInstance();
		if (testClass instanceof BaseTest) {
			hook.accept((BaseTest) testClass);
		}
	}
	
	@Override
	public void afterAll(ExtensionContext extensionContext) {
		BFLogger.logDebug(getClass().getName() + ".observers: " + observers.get()
				.toString());
		
		BFLogger.logDebug(getClass().getName() + ".classObservers: " + classObservers.get()
				.toString());
		
		observers.get()
				.forEach(ITestObserver::onTestClassFinish);
		classObservers.get()
				.forEach(ITestObserver::onTestClassFinish);
		
		observers.get()
				.clear();
		classObservers.get()
				.clear();
		
		BFLogger.logDebug("All observers cleared.");
	}
	
	private void printTimeExecutionLog(String testName) {
		BFLogger.logInfo("\"" + testName + "\"" + getFormattedTestDuration());
	}
	
	private String getFormattedTestDuration() {
		return String.format(" - DURATION: %1.2f min", (float) stopwatch.get() / (60 * 1000));
	}
	
	@Override
	public void handleBeforeAllMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
		logExceptionInfo(context, throwable, "@BeforeAll");
	}
	
	@Override
	public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
		logExceptionInfo(context, throwable, "@BeforeEach");
	}
	
	@Override
	public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
		logExceptionInfo(context, throwable, "@Test");
	}
	
	@Override
	public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
		logExceptionInfo(context, throwable, "@AfterEach");
	}
	
	@Override
	public void handleAfterAllMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
		try {
			logExceptionInfo(context, throwable, "@AfterAll");
		} catch (Throwable e) {
			if (DONT_CONSUME_EXCEPTION_IN_AFTERALL)
				throw throwable;
		}
	}
	
	private void logExceptionInfo(ExtensionContext context, Throwable throwable, String annotationName) throws Throwable {
		String className = context.getRequiredTestClass()
				.getName();
		String testName = context.getDisplayName();
		BFLogger.logError("\"" + className + "#" + testName + "\"" + " - EXCEPTION in " + annotationName + ": " + throwable.getMessage());
		throw throwable;
	}
	
	@Override
	public void addObserver(ITestObserver observer) {
		BFLogger.logDebug("To add observer: " + observer.toString());
		
		boolean anyMatchTestClassObservers = isObserverAlreadyAdded(classObservers.get(), observer);
		boolean anyMatchMethodObservers = isObserverAlreadyAdded(observers.get(), observer);
		
		BFLogger.logDebug(getClass().getName() + ".observers: " + observers.get()
				.toString());
		BFLogger.logDebug(getClass().getName() + ".classObservers: " + classObservers.get()
				.toString());
		
		if (!(anyMatchMethodObservers | anyMatchTestClassObservers)) {
			if (isAddedFromBeforeAllMethod()) {
				classObservers.get()
						.add(observer);
			} else {
				observers.get()
						.add(observer);
			}
			BFLogger.logDebug("Added observer: " + observer.toString());
		}
	}
	
	private boolean isObserverAlreadyAdded(List<ITestObserver> observers, ITestObserver observer) {
		return observers.stream()
				.anyMatch(x -> x.getModuleType()
						.equals(observer.getModuleType()));
	}
	
	private static boolean isAddedFromBeforeAllMethod() {
		for (StackTraceElement elem : Thread.currentThread()
				.getStackTrace()) {
			try {
				Method method = Class.forName(elem.getClassName())
						.getDeclaredMethod(elem.getMethodName());
				if (method.getDeclaredAnnotation(BeforeAll.class) != null) {
					return true;
				}
			} catch (SecurityException | ClassNotFoundException e) {
				BFLogger.logDebug(e.getMessage());
			} catch (NoSuchMethodException e) {
				// do nothing
			}
		}
		
		return false;
	}
	
	@Override
	public void removeObserver(ITestObserver observer) {
		BFLogger.logDebug("To remove observer: " + observer.toString());
		
		if (classObservers.get()
				.contains(observer)) {
			classObservers.get()
					.remove(observer);
			BFLogger.logDebug("Removed observer: " + observer.toString());
		} else {
			// There must be at least one observer when finishing class
			if (!classObservers.get()
					.isEmpty()) {
				observers.get()
						.remove(observer);
				BFLogger.logDebug("Removed observer: " + observer.toString());
			}
		}
	}
}