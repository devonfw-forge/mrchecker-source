package com.capgemini.mrchecker.webapi.core.base.driver;


import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.webapi.core.base.runtime.RuntimeParameters;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.io.*;

public class DriverManagerTest_standaloneWiremock extends BaseTest {

	static Process p = null;

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

	@BeforeClass
	public static void beforeAll(){
		File file = new File("c:\\Users\\lucst\\Downloads\\wiremock-standalone-2.21.0.jar");

		ProcessBuilder pb = new ProcessBuilder("java", "-jar", file.getAbsolutePath(), "--port",  "8100");
		try {
			pb.redirectOutput(new File("c:\\Users\\lucst\\Downloads\\output.log"));
			p = pb.start();

			OutputStream out = p.getOutputStream();
			ByteArrayOutputStream byte1=new ByteArrayOutputStream();
			out.write(byte1.toByteArray());
			String s=byte1.toString();
			System.out.println("jj: " +s);

			InputStream in = p.getInputStream();
			InputStream err = p.getErrorStream();

			byte b[]=new byte[in.available()];
			in.read(b,0,b.length);
			System.out.println("Input:" + new String(b));

			byte c[]=new byte[err.available()];
			err.read(c,0,c.length);
			System.out.println("Error:"  + new String(c));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@AfterClass
	public static void afterAll(){
		try {
			p.destroyForcibly();
		}catch (Exception e){

		}

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
