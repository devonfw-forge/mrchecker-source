package com.capgemini.mrchecker.webapi.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesFileSettings {
    private boolean isVirtualServerEnabled = true;

    @Inject(optional = true)
    private void setVirtualServerEnabled(@Named("webapi.isVirtualServerEnabled") boolean value) {
        isVirtualServerEnabled = value;
    }

    public boolean isVirtualServerEnabled() {
        return isVirtualServerEnabled;
    }
}