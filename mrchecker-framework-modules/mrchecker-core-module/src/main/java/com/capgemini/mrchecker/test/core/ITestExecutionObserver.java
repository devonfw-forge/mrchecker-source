package com.capgemini.mrchecker.test.core;

import org.junit.jupiter.api.extension.*;

public interface ITestExecutionObserver extends BeforeAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, TestWatcher, AfterAllCallback, ITestObservable {
	// aggregating interface
}
