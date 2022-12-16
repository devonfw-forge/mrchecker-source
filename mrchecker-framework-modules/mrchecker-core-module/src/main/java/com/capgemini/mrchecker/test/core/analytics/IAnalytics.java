package com.capgemini.mrchecker.test.core.analytics;

public interface IAnalytics {

    @SuppressWarnings("unused")
    default void sendClassName(String packageName, String className, String description) {
    }

    default void sendClassName() {
        String packageName = Thread.currentThread()
                .getStackTrace()[2].getClassName();
        String className = Thread.currentThread()
                .getStackTrace()[2].getMethodName();
        sendClassName(packageName, className, "");
    }

    @SuppressWarnings("unused")
    default void sendMethodEvent(String analyticsCategoryName, String eventName) {
    }

    default void sendMethodEvent(String analyticsCategoryName) {
        String eventName = Thread.currentThread()
                .getStackTrace()[2].getMethodName();
        sendMethodEvent(analyticsCategoryName, eventName);
    }

    void setInstance();
}
