package com.capgemini.mrchecker.webapi.pages.httbin;

import com.capgemini.mrchecker.webapi.core.BasePageWebAPI;
import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.pages.environment.GetEnvironmentParam;
import io.restassured.response.Response;

public class RandomNBytesPage extends BasePageWebAPI {
	private final static String HOSTNAME = GetEnvironmentParam.HTTPBIN.getValue();
	private final static String PATH     = "/bytes/{length}";
	private final static String ENDPOINT = HOSTNAME + PATH;

	public Response sendGETQuery(String length) {
		return DriverManager.getDriverWebAPI()
				.given().pathParam("length", length)
				.get(ENDPOINT);
	}

	public byte[] getRandomBytes(Response response) {
		return response.body().asByteArray();
	}

	@Override
	public String getEndpoint() {
		return ENDPOINT;
	}
}
