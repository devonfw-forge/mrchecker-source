package com.capgemini.mrchecker.webapi.pages.httbin;

import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.pages.environment.GetEnvironmentParam;
import com.capgemini.mrchecker.webapi.core.BasePageWebAPI;
import io.restassured.response.Response;
public class RandomNBytesPage extends BasePageWebAPI  {
	private final static String HOSTNAME = GetEnvironmentParam.HTTPBIN.getValue();
	private final static String PATH     = "/bytes/{value}";
	private final static String ENDPOINT = HOSTNAME + PATH;

	public Response sendGETQuery(String value) {
		return DriverManager.getDriverWebAPI()
				.given().pathParam("value", value)
				.get(ENDPOINT);
	}

	@Override
	public String getEndpoint() {
		return ENDPOINT;
	}
}
