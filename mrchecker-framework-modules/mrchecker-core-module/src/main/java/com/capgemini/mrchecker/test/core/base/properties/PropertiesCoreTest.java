package com.capgemini.mrchecker.test.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesCoreTest {
	
	private String	coreDefaultEnvironmentName	= "DEV";
	private boolean	coreIsAnalyticsEnabled		= true;
	private boolean	coreIsEncryptionEnabled		= false;
	
	@Inject(optional = true)
	@SuppressWarnings("unused")
	private void setIsAnalyticsEnabled(@Named("core.isAnalyticsEnabled") String status) {
		this.coreIsAnalyticsEnabled = !status.toLowerCase()
				.equals("false");
	}
	
	@Inject(optional = true)
	@SuppressWarnings("unused")
	private void setIsEncryptionEnabled(@Named("core.isEncryptionEnabled") String status) {
		this.coreIsEncryptionEnabled = status.toLowerCase()
				.equals("true");
	}
	
	@Inject(optional = true)
	@SuppressWarnings("unused")
	private void setDefaultEnvironmentName(@Named("core.defaultEnvironmentName") String coreDefaultEnvironmentName) {
		this.coreDefaultEnvironmentName = coreDefaultEnvironmentName;
	}
	
	public boolean isAnalyticsEnabled() {
		return coreIsAnalyticsEnabled;
	}
	
	public boolean isEncryptionEnabled() {
		return coreIsEncryptionEnabled;
	}
	
	public String getDefaultEnvironmentName() {
		return coreDefaultEnvironmentName;
	}
}