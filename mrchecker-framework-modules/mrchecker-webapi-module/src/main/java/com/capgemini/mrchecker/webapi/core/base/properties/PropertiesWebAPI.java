package com.capgemini.mrchecker.webapi.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesWebAPI {
    private boolean isVirtualServerEnabled = true;
    private boolean allowStaticEndpoint = false;

    @Inject(optional = true)
    private void setVirtualServerEnabled(@Named("webapi.isVirtualServerEnabled") boolean value) {
        isVirtualServerEnabled = value;
    }

    public boolean isVirtualServerEnabled() {
        return isVirtualServerEnabled;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setAllowStaticEndpoint(@Named("webapi.allowStaticEndpoint") boolean value) {
        this.allowStaticEndpoint = value;
    }

    public boolean getAllowStaticEndpoint() {
        return allowStaticEndpoint;
    }
}