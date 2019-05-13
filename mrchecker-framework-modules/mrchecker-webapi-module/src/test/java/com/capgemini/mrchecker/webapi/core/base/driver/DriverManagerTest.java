package com.capgemini.mrchecker.webapi.core.base.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Test;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.webapi.core.base.runtime.RuntimeParameters;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.FatalStartupException;

public class DriverManagerTest extends BaseTest {

	@Override
	public void setUp() {
		DriverManager.closeDriverVirtualServer();
	}

	@Override
	public void tearDown() {
		DriverManager.closeDriverVirtualServer();
	}


	@AfterClass
	public static void afterAll(){
		DriverManager.clearAllDrivers();
	}

	@Test
	public void testRuntimeEnvironmentHostHttp() {
		System.setProperty("mock_http_host", "http://test.org");
		RuntimeParameters.MOCK_HTTP_HOST.refreshParameterValue();
		assertEquals("System parameters for 'mock_http_host' should be 'http://test.org'", "http://test.org", RuntimeParameters.MOCK_HTTP_HOST.getValue());
	}
	
	@Test
	public void testRuntimeEnvironmentHostHttpDefaultValue() {
		System.clearProperty("mock_http_host");
		RuntimeParameters.MOCK_HTTP_HOST.refreshParameterValue();
		assertEquals("System parameters for 'mock_http_host' should be 'http://localhost'", "http://localhost", RuntimeParameters.MOCK_HTTP_HOST.getValue());
	}
	
	@Test
	public void testRuntimeEnvironmentPortHttp() {
		System.setProperty("mock_http_port", "8080");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		assertEquals("System parameters for 'mock_http_port' should be '8080'", "8080", RuntimeParameters.MOCK_HTTP_PORT.getValue());
	}
	
	@Test
	public void testRuntimeEnvironmentPortHttpDefaultValue() {
		System.clearProperty("mock_http_port");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		assertEquals("Default value for system parameters 'mock_http_port' should be ''", "", RuntimeParameters.MOCK_HTTP_PORT.getValue());
	}
	
	@Test
	public void testWireMockStartPortHttpRandomPortHttps() {
		System.setProperty("mock_http_port", "8090");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		assertEquals("Runtime Parameter for MOCK_HTTP_PORT is incorrect", "8090", RuntimeParameters.MOCK_HTTP_PORT.getValue());
		DriverManager.getDriverVirtualService();
		WireMockServer driver = DriverManager.getDriverVirtualServerService();
		assertEquals("Mock server for http does not run o specified port", 8090, driver.port());
	}
	
	@Test
	public void testWireMockStartTwoServersWithTheSameHttpPort() {
		System.setProperty("mock_http_port", "8081");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		
		WireMockServer driver1 = null;
		WireMockServer driver2 = null;
		DriverManager.closeDriverVirtualServer();
		try {
			// Start #1 server
			driver1 = DriverManager.getDriverVirtualServerService();
			assertEquals("Mock server for http does not run o port 8081", 8081, driver1.port());
			
			// Enable to add new server instances in this thread. Simulation of multi thread and bind to the same port
			DriverManager.clearAllDrivers();
			
			// Start #2 server
			driver2 = DriverManager.getDriverVirtualServerService();
			assertEquals("Mock server for http does not run o port 8081", 8081, driver2.port());
		} catch (FatalStartupException e) {
			assertTrue("No information about bind error", e.getMessage()
					.contains("Address already in use: bind"));
		} finally {
			// Close all drivers
			try {
				driver1.shutdown();
				driver2.shutdown();
			} catch (Exception e) {
			}
		}
	}
	

}
