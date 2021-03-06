package com.capgemini.mrchecker.test.core;

public interface ITestObserver {
	void onTestSuccess();
	
	void onTestFailure();
	
	void onTestFinish();
	
	void onTestClassFinish();
	
	void addToTestExecutionObserver();
	
	ModuleType getModuleType();
}
