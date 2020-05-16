package com.capgemini.mrchecker.test.core.analytics;

/**
 * This class stores various system properties
 * 
 * @author LUSTEFAN
 */
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
