package com.capgemini.mrchecker.test.core;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

public abstract class Page implements ITestObserver {
	
	protected static final ITestExecutionObserver TEST_EXECUTION_OBSERVER = TestExecutionObserver.getInstance();
	
	protected Page() {
	}
	
	@Override
	public void onTestSuccess() {
		BFLogger.logDebug("Page.onTestSuccess    " + getClass()
				.getSimpleName());
	}
	
	@Override
	public void onTestFailure() {
		BFLogger.logDebug("Page.onTestFailure    " + getClass()
				.getSimpleName());
	}
	
	@Override
	public void onTestFinish() {
		BFLogger.logDebug("Page.onTestFinish    " + getClass()
				.getSimpleName());
		
		TEST_EXECUTION_OBSERVER.removeObserver(this);
	}
	
	@Override
	public void onTestClassFinish() {
		BFLogger.logDebug("Page.onTestClassFinish    " + getClass()
				.getSimpleName());
	}
	
	@Override
	public final void addToTestExecutionObserver() {
		TEST_EXECUTION_OBSERVER.addObserver(this);
	}
}
