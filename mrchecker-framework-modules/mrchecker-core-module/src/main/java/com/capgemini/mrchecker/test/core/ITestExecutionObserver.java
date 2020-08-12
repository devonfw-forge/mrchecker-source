package com.capgemini.mrchecker.test.core;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;

public interface ITestExecutionObserver
		extends BeforeAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, TestWatcher, AfterAllCallback, LifecycleMethodExecutionExceptionHandler, TestExecutionExceptionHandler,
		ITestObservable {
	// aggregating interface
}
