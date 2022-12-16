package com.capgemini.mrchecker.test.core.analytics;

public enum AnalyticsProvider implements IAnalytics {

    DISABLED() {
        @Override
        public void setInstance() {
        }
    };

    AnalyticsProvider() {
        setInstance();
    }
}
