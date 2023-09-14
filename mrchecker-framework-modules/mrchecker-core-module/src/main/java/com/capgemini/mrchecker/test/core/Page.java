package com.capgemini.mrchecker.test.core;

import com.capgemini.mrchecker.test.core.exceptions.BFStaticVariableException;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class Page implements ITestObserver {
    protected static final ITestExecutionObserver TEST_EXECUTION_OBSERVER = TestExecutionObserver.getInstance();

    private ExtensionContext context;

    protected Page() {
    }

    @Override
    public void onTestDisabled() {
        BFLogger.logDebug("Page.onTestDisabled    " + getClass().getSimpleName());
    }

    @Override
    public void onTestSuccess() {
        BFLogger.logDebug("Page.onTestSuccess    " + getClass().getSimpleName());
    }

    @Override
    public void onTestAborted() {
        BFLogger.logDebug("Page.onTestAborted    " + getClass().getSimpleName());
    }

    @Override
    public void onTestFailed() {
        BFLogger.logDebug("Page.onTestFailed    " + getClass().getSimpleName());
    }

    @Override
    public void onTestFinish() {
        BFLogger.logDebug("Page.onTestFinish    " + getClass().getSimpleName());
        TEST_EXECUTION_OBSERVER.removeObserver(this);
    }

    @Override
    public void onSetupFailure() {
        BFLogger.logDebug("Page.onSetupFailure    " + getClass().getSimpleName());
    }

    @Override
    public void onTeardownFailure() {
        BFLogger.logDebug("Page.onTeardownFailure    " + getClass().getSimpleName());
    }


    @Override
    public void onTestClassFinish() {
        BFLogger.logDebug("Page.onTestClassFinish    " + getClass().getSimpleName());
    }

    @Override
    public void onBeforeAllException() {
        BFLogger.logDebug("Page.onBeforeAllException    " + getClass().getSimpleName());
    }

    @Override
    public void onBeforeEachException() {
        BFLogger.logDebug("Page.onBeforeEachException    " + getClass().getSimpleName());
    }

    @Override
    public void onAfterAllException() {
        BFLogger.logDebug("Page.onAfterAllException    " + getClass().getSimpleName());
    }

    @Override
    public void onAfterEachException() {
        BFLogger.logDebug("Page.onAfterEachException    " + getClass().getSimpleName());
    }

    @Override
    public void onTestExecutionException() {
        BFLogger.logDebug("Page.onTestExecutionException    " + getClass().getSimpleName());
    }

    @Override
    public final void addToTestExecutionObserver() {
        TEST_EXECUTION_OBSERVER.addObserver(this);
    }

    protected void verifyStaticObject(boolean allowStaticObject, String objectName) {
        if (!allowStaticObject) {
            StackTraceElement[] stackTrace = new Exception().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (stackTraceElement.getMethodName().equalsIgnoreCase("<clinit>")) {
                    throw new BFStaticVariableException(objectName);
                }
            }
        }
    }

    @Override
    public void setExtensionContext(ExtensionContext context) {
        this.context = context;
    }

    @Override
    public ExtensionContext getExtensionContext() {
        return context;
    }
}