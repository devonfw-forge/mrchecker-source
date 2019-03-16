package com.capgemini.mrchecker.webapi.pages.httbin.cookies;

import com.capgemini.mrchecker.webapi.core.BasePageWebAPI;
import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.pages.environment.GetEnvironmentParam;

import io.restassured.response.Response;

public class GetCookiesPage extends BasePageWebAPI {
	private final static String	HOSTNAME	= GetEnvironmentParam.HTTPBIN.getValue();
	private final static String	PATH		= "/cookies";
	private final static String	ENDPOINT	= HOSTNAME + PATH;
	
	public Response getCookies() {
		return DriverManager.getDriverWebAPI()
				.filter(CookieSession.getSession())
				.when()
				.get(ENDPOINT);
	}
	
	public String getEndpoint() {
		return ENDPOINT;
	}
}