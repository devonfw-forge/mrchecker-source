package com.capgemini.mrchecker.webapi.unit.core.base.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.webapi.tags.UnitTest;
import com.capgemini.mrchecker.webapi.unit.core.base.runtime.RuntimeParameters;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.FatalStartupException;

@UnitTest
@ResourceLock("PropertiesFileSettings.class")
public class DriverManagerTest extends BaseTest {
	
	WireMockServer driver;
	
	@Override
	public void setUp() {
		DriverManager.closeDriverVirtualServer();
		driver = null;
	}
	
	@Override
	public void tearDown() {
		DriverManager.closeDriverVirtualServer();
		
		System.clearProperty("mock_http_host");
		RuntimeParameters.MOCK_HTTP_HOST.refreshParameterValue();
		
		System.clearProperty("mock_http_port");
		RuntimeParameters.MOCK_HTTP_PORT.refreshParameterValue();
		
		try {
			driver.shutdown();
		} catch (Exception e) {
		}
	}
	
	@AfterAll
	public static void afterAll() {
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
		
		DriverManager.closeDriverVirtualServer();
		
		// Start #1 server
		driver = DriverManager.getDriverVirtualServerService();
		assertEquals("Mock server for http does not run o port 8081", 8081, driver.port());
		
		// Enable to add new server instances in this thread. Simulation of multi thread and bind to the same port
		DriverManager.clearAllDrivers();
		
		// Start #2 server and expect Error
		assertThrows(FatalStartupException.class, DriverManager::getDriverVirtualServerService);
		// thrown.expectMessage(containsString("Address already in use"));
		assertEquals("Mock server for http does not run o port 8081", 8081, driver.port());
	}
	
}
