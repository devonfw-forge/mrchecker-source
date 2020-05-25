package com.capgemini.mrchecker.test.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

import io.qameta.allure.Attachment;

public class BaseTestExecutionObserver implements ITestExecutionObserver {
	
	private static BaseTestExecutionObserver instance;
	
	private final ThreadLocal<Long> stopwatch = ThreadLocal.withInitial(() -> 0L);
	
	private final ThreadLocal<List<ITestObserver>>	observers		= ThreadLocal.withInitial(ArrayList::new);
	private final ThreadLocal<List<ITestObserver>>	classObservers	= ThreadLocal.withInitial(ArrayList::new);
	
	private BaseTestExecutionObserver() {
	}
	
	public static BaseTestExecutionObserver getInstance() {
		if (Objects.isNull(instance)) {
			synchronized (BaseTestExecutionObserver.class) {
				if (Objects.isNull(instance)) {
					instance = new BaseTestExecutionObserver();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void beforeAll(ExtensionContext extensionContext) {
		String testName = extensionContext.getTestClass()
				.get()
				.getName();
		BFLogger.logInfo("\"" + testName + "\"" + " - CLASS STARTED.");
	}
	
	@Override
	public void beforeTestExecution(ExtensionContext extensionContext) {
		BFLogger.RestrictedMethods.startSeparateLog();
		String testName = extensionContext.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " STARTED.");
		BaseTest.getAnalytics()
				.sendClassName();
		((IBaseTest) extensionContext.getRequiredTestInstance()).setUp();
		stopwatch.set(System.currentTimeMillis());
	}
	
	@Override
	public void afterTestExecution(ExtensionContext extensionContext) {
		stopwatch.set(System.currentTimeMillis() - stopwatch.get()); // end timing
		String testName = extensionContext.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - FINISHED.");
		printTimeExecutionLog(testName);
		((IBaseTest) extensionContext.getRequiredTestInstance()).tearDown();
		makeLogForTest();
	}
	
	// TODO: fix that
	@Attachment("Log file")
	public void makeLogForTest() {
		BFLogger.RestrictedMethods.dumpSeparateLog();
	}
	
	@Override
	public void testDisabled(ExtensionContext context, Optional<String> reason) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - DISABLED.");
		// TODO: add reason
	}
	
	@Override
	public void testSuccessful(ExtensionContext context) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - PASSED.");
		observers.get()
				.forEach(ITestObserver::onTestSuccess);
		afterEach();
		classObservers.get()
				.forEach(ITestObserver::onTestSuccess);
	}
	
	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - ABORTED.");
		afterEach();
	}
	
	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		String testName = context.getDisplayName();
		BFLogger.logInfo("\"" + testName + "\"" + " - FAILED.");
		observers.get()
				.forEach(ITestObserver::onTestFailure);
		afterEach();
		classObservers.get()
				.forEach(ITestObserver::onTestFailure);
		
	}
	
	private void afterEach() {
		observers.get()
				.forEach(ITestObserver::onTestFinish);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				continue;
			}
		}
		
		return false;
	}
	
	@Override
	public void removeObserver(ITestObserver observer) {
		BFLogger.logDebug("To remove observer: " + observer.toString());
		
		if (isAddedFromBeforeAllMethod()) {
			classObservers.get()
					.remove(observer);
			BFLogger.logDebug("Removed observer: " + observer.toString());
		} else {
			if (!classObservers.get()
					.isEmpty()) {
				observers.get()
						.remove(observer);
				BFLogger.logDebug("Removed observer: " + observer.toString());
			}
		}
	}
}