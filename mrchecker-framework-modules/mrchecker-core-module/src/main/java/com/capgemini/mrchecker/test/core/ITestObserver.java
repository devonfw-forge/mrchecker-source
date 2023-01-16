package com.capgemini.mrchecker.test.core;

public interface ITestObserver {
    void onTestSuccess();

    void onTestFailure();

    void onTestFinish();

    void onSetupFailure();

    void onTeardownFailure();

    void onTestClassFinish();

    void addToTestExecutionObserver();

    ModuleType getModuleType();
}