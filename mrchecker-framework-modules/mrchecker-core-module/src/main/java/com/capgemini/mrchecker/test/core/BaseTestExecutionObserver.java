package com.capgemini.mrchecker.test.core;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

import io.qameta.allure.Attachment;

public class BaseTestExecutionObserver implements ITestExecutionObserver {
	
	// TODO: fix multi thread
	private final ThreadLocal<Long> stopwatch = ThreadLocal.withInitial(() -> 0L);
	
	private static final TestObserversManager TEST_OBSERVERS_MANAGER = TestObserversManager.getInstance();
	
	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		String testName = extensionContext.getTestClass()
				.get()
				.getName();
		BFLogger.logInfo("\"" + testName + "\"" + " - CLASS STARTED.");
	}
	
	@Override
	public void beforeTestExecution(ExtensionContext extensionContext) {
		BFLogger.RestrictedMethods.startSeparateLog();
		String testName = extensionContext.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - STARTED.");
		stopwatch.set(System.currentTimeMillis());
		BaseTest.getAnalytics()
				.sendClassName();
		((IBaseTest) extensionContext.getRequiredTestInstance()).setUp();
	}
	
	@Override
	public void testDisabled(ExtensionContext context, Optional<String> reason) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - DISABLED.");
	}
	
	@Override
	public void testSuccessful(ExtensionContext context) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - PASSED.");
		TEST_OBSERVERS_MANAGER.getAllObservers()
				.forEach(ITestObserver::onTestSuccess);
		// classObservers.get()
		// .forEach(ITestObserver::onTestSuccess);
		// observers.get()
		// .forEach(ITestObserver::onTestSuccess);
	}
	
	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - ABORTED.");
	}
	
	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - FAILED.");
		TEST_OBSERVERS_MANAGER.getAllObservers()
				.forEach(ITestObserver::onTestFailure);
		// classObservers.get()
		// .forEach(ITestObserver::onTestFailure);
		// observers.get()
		// .forEach(ITestObserver::onTestFailure);
	}
	
	@Override
	public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
		stopwatch.set(System.currentTimeMillis() - stopwatch.get()); // end timing
		String testName = extensionContext.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - FINISHED.");
		printTimeExecutionLog(testName);
		((IBaseTest) extensionContext.getRequiredTestInstance()).tearDown();
		makeLogForTest();
		TEST_OBSERVERS_MANAGER.getAllObservers()
				.forEach(ITestObserver::onTestFinish);
		// observers.get()
		// .forEach(ITestObserver::onTestFinish);
	}
	
	// TODO: fix that
	@Attachment("Log file")
	public void makeLogForTest() {
		BFLogger.RestrictedMethods.dumpSeparateLog();
	}
	
	@Override
	public void afterAll(ExtensionContext extensionContext) {
		BFLogger.logDebug(getClass().getName() + ".observers: " + TEST_OBSERVERS_MANAGER.getAllObservers()
				.toString());
		
		// BFLogger.logDebug(getClass().getName() + ".observers: " + observers.get()
		// .toString());
		// BFLogger.logDebug(getClass().getName() + ".classObservers: " + classObservers.get()
		// .toString());
		
		TEST_OBSERVERS_MANAGER.getAllObservers()
				.forEach(ITestObserver::onTestClassFinish);
		// classObservers.get()
		// .forEach(ITestObserver::onTestClassFinish);
		// observers.get()
		// .forEach(ITestObserver::onTestClassFinish);
		
		TEST_OBSERVERS_MANAGER.removeAllObservers();
		// observers.get()
		// .clear();
		// classObservers.get()
		// .clear();
		
		BFLogger.logDebug("All observers cleared.");
	}
	
	private void printTimeExecutionLog(String testName) {
		BFLogger.logInfo("\"" + testName + "\"" + getFormattedTestDuration());
	}
	
	private String getFormattedTestDuration() {
		return String.format(" - DURATION: %1.2f min", (float) stopwatch.get() / (60 * 1000));
	}
	
	// @Override
	// public void addObserver(ITestObserver observer) {
	// BFLogger.logDebug("To add observer: " + observer.toString());
	//
	// boolean anyMatchTestClassObservers = isObserverAlreadyAdded(classObservers.get(), observer);
	// boolean anyMatchMethodObservers = isObserverAlreadyAdded(observers.get(), observer);
	//
	// BFLogger.logDebug(getClass().getName() + ".observers: " + observers.get()
	// .toString());
	// BFLogger.logDebug(getClass().getName() + "classObservers: " + classObservers.get()
	// .toString());
	//
	// if (!(anyMatchMethodObservers | anyMatchTestClassObservers)) {
	// if (isAddedFromBeforeAllMethod()) {
	// classObservers.get()
	// .add(observer);
	// } else {
	// observers.get()
	// .add(observer);
	// }
	// BFLogger.logDebug("Added observer: " + observer.toString());
	// }
	// }
	//
	// private boolean isObserverAlreadyAdded(List<ITestObserver> observers, ITestObserver observer) {
	// return observers.stream()
	// .anyMatch(x -> x.getModuleType()
	// .equals(observer.getModuleType()));
	// }
	//
	// private static boolean isAddedFromBeforeAllMethod() {
	// for (StackTraceElement elem : Thread.currentThread()
	// .getStackTrace()) {
	// try {
	// Method method = Class.forName(elem.getClassName())
	// .getDeclaredMethod(elem.getMethodName());
	// if (method.getDeclaredAnnotation(BeforeAll.class) != null) {
	// return true;
	// }
	// } catch (SecurityException | ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// continue;
	// }
	// }
	//
	// return false;
	// }
	//
	// @Override
	// public void removeObserver(ITestObserver observer) {
	// BFLogger.logDebug("To remove observer: " + observer.toString());
	//
	// if (isAddedFromBeforeAllMethod()) {
	// classObservers.get()
	// .remove(observer);
	// BFLogger.logDebug("Removed observer: " + observer.toString());
	// } else {
	// if (classObservers.get()
	// .isEmpty()) {
	// observers.get()
	// .remove(observer);
	// BFLogger.logDebug("Removed observer: " + observer.toString());
	// }
	// }
	// }
}