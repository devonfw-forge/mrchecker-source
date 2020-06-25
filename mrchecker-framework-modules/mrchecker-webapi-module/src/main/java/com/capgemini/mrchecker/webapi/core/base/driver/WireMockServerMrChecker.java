package com.capgemini.mrchecker.webapi.core.base.driver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Container;
import com.github.tomakehurst.wiremock.core.Options;

public class WireMockServerMrChecker extends WireMockServer implements Container {
	
	public WireMockServerMrChecker(Options options) {
		super(options);
	}
	
	public WireMock getClient() {
		return super.client;
	}
	
}
