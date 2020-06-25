package com.capgemini.mrchecker.webapi.core.base.driver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

public class VirtualizedService {
	
	private final WireMock			driver;
	private final WireMockServer	driverServer;
	private final String			httpHost;
	private final int				httpPort;
	
	VirtualizedService(WireMock driver, WireMockServer driverServer, String httpHost, int httpPort) {
		this.driver = driver;
		this.driverServer = driverServer;
		this.httpHost = httpHost;
		this.httpPort = httpPort;
		
	}
	
	public WireMock getDriver() {
		return driver;
	}
	
	public WireMockServer getDriverServer() {
		return driverServer;
	}
	
	public int getHttpPort() {
		return httpPort;
	}
	
	public String getHttpHost() {
		return httpHost;
	}
	
	public String getEndpointBaseUri() {
		return getHttpHost() + ":" + getHttpPort();
	}
	
	@Override
	public String toString() {
		return "Service for host " + getEndpointBaseUri();
	}
};
