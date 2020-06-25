package com.capgemini.mrchecker.webapi.unit.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesFileSettings {
	
	private boolean isVirtualServerEnabled = true;
	
	@Inject(optional = true)
	private void setProperty_isVirtualServerEnabled(@Named("webapi.isVirtualServerEnabled") String value) {
		this.isVirtualServerEnabled = !value.toLowerCase()
				.equals("false");
	}
	
	public boolean isVirtualServerEnabled() {
		return isVirtualServerEnabled;
	}
	
}
