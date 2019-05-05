package com.capgemini.mrchecker.webapi.core.base.driver;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.webapi.core.base.runtime.RuntimeParameters;
import com.github.tomakehurst.wiremock.client.WireMock;

public class DriverManagerTest_standaloneWiremock extends BaseTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testStandaloneWireMockStartDedicatedPortHttp() {
		System.setProperty("mock_http_host", "192.168.1.104");
		RuntimeParameters.MOCK_HTTP_HOST.refreshParameterValue();
		assertEquals("System parameters for 'mock_http_host'", "192.168.1.104", RuntimeParameters.MOCK_HTTP_HOST.getValue());
		
		System.setProperty("mock_http_port", "8100");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		assertEquals("Runtime Parameter for MOCK_HTTP_PORT is incorrect", "8100", RuntimeParameters.MOCK_HTTP_PORT.getValue());
		
		WireMock driver = DriverManager.getDriverVirtualService();
		assertEquals("Mock server for http does not run o specified port", 8100, DriverManager.getHttpPort());
		assertEquals("Mock server for http does not run o specific hostname", "192.168.1.104", DriverManager.getHttpHost());
	}
	
	@Test
	public void testStandaloneWireMockWrongHostname() {
		System.setProperty("mock_http_host", "http://wrongHost");
		RuntimeParameters.MOCK_HTTP_HOST.refreshParameterValue();
		
		System.setProperty("mock_http_port", "8100");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		
		thrown.expect(java.net.UnknownHostException.class);
		thrown.expectMessage(startsWith("http"));
		WireMock driver = DriverManager.getDriverVirtualService();
	}
	
	@Test
	public void testStandaloneWireMockStartRandomPortHttp() {
		System.setProperty("mock_http_host", "http://127.0.0.1");
		RuntimeParameters.MOCK_HTTP_HOST.refreshParameterValue();
		
		System.setProperty("mock_http_port", "1000");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		
		thrown.expect(java.net.UnknownHostException.class);
		thrown.expectMessage(startsWith("http"));
		WireMock driver = DriverManager.getDriverVirtualService();
	}
	
	@Override
	public void setUp() {
		DriverManager.closeDriverVirtualServer();
	}
	
	@Override
	public void tearDown() {
		DriverManager.closeDriverVirtualServer();
	}
	
}
