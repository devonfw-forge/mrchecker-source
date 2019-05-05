package com.capgemini.mrchecker.endpoint.rest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.endpoint.stubs.StubREST_Builder;
import com.github.tomakehurst.wiremock.client.WireMock;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class REST_FarenheitToCelsiusMethod_DynamicResponse extends BaseTest {
	
	private static String endpointBaseUri;
	
	@BeforeClass
	public static void beforeClass() {
		
		// Start Virtual Server
		WireMock driverVirtualService = DriverManager.getDriverVirtualService();
		
		// Get Virtual Server running http and https ports
		int httpPort = DriverManager.getHttpPort();
		String baseURI = DriverManager.getHttpHost();
		endpointBaseUri = baseURI + ":" + httpPort;
	}
	
	@Override
	public void setUp() {
	}
	
	@Override
	public void tearDown() {
	}
	
	@Test
	public void testRestMock_post_ResponseBodyBasedOnRequestBody() throws Exception {
		
		/*
		 * ----------
		 * Mock response. Map request with virtual asset from file
		 * -----------
		 */
		BFLogger.logInfo("#1 Create Stub content message");
		BFLogger.logInfo("#2 Add resource to wiremock server");
		new StubREST_Builder.StubBuilder("/some/thing")
				.setResponse("{ \"FahrenheitToCelsiusResponse\":{\"FahrenheitToCelsiusResult\":$(FahrenheitToCelsius.Fahrenheit)}}")
				.setStatusCode(200)
				.build();
		
		/*
		 * ----------
		 * Time to validate virtual response. Send POST request and validate response
		 * -----------
		 */
		BFLogger.logInfo("#3 Send request to generated stub");
		Response response = DriverManager.getDriverWebAPI()
				.with()
				.header("Content-Type", ContentType.JSON.toString())
				.body("{\"FahrenheitToCelsius\":{\"Fahrenheit\":500}}")
				.log()
				.all()
				.when()
				.post(endpointBaseUri + "/some/thing")
				.thenReturn();
		
		BFLogger.logInfo("#4 Validate response ");
		BFLogger.logDebug("/some/thing: " + response.jsonPath()
				.prettyPrint());
		assertThat(response.statusCode(), is(200));
		assertThat(response.body()
				.jsonPath()
				.get("FahrenheitToCelsiusResponse.FahrenheitToCelsiusResult"), is(500));
	}
	
	@Test
	public void testRestMock_post_ResponseBodyBasedOnRequestUrlArgs() throws Exception {
		
		/*
		 * ----------
		 * Mock response. Map request with virtual asset from file
		 * -----------
		 */
		BFLogger.logInfo("#1 Create Stub content message");
		BFLogger.logInfo("#2 Add resource to wiremock server");
		new StubREST_Builder.StubBuilder("/some/thing.*")
				.setResponse("{ \"FahrenheitToCelsiusResponse\":{\"FahrenheitToCelsiusResult\":$(one), \"Value2\":\"$(two)\"} }")
				.setStatusCode(200)
				.build();
		
		/*
		 * ----------
		 * Time to validate virtual response. Send POST request and validate response
		 * -----------
		 */
		BFLogger.logInfo("#3 Send request to generated stub");
		Response response = DriverManager.getDriverWebAPI()
				.with()
				.header("Content-Type", ContentType.JSON.toString())
				.log()
				.all()
				.when()
				.post(endpointBaseUri + "/some/thing?one=100&two=HelloWorld")
				.thenReturn();
		
		BFLogger.logInfo("#4 Validate response ");
		BFLogger.logDebug("/some/thing?one=100&two=HelloWorld: " + response.jsonPath()
				.prettyPrint());
		assertThat(response.statusCode(), is(200));
		assertThat(response.getBody()
				.jsonPath()
				.get("FahrenheitToCelsiusResponse.FahrenheitToCelsiusResult"), is(100));
		assertThat(response.getBody()
				.jsonPath()
				.get("FahrenheitToCelsiusResponse.Value2"), is("HelloWorld"));
	}
	
}
