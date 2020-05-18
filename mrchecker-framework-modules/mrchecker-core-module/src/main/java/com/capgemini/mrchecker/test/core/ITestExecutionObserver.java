package com.capgemini.mrchecker.test.core;

import org.junit.jupiter.api.extension.*;

public interface ITestExecutionObserver extends BeforeAllCallback, BeforeTestExecutionCallback, TestWatcher, AfterTestExecutionCallback, AfterAllCallback {
	// aggregating interface
}
