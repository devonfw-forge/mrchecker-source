package com.capgemini.mrchecker.webapi.pages.httbin.cookies;

import com.capgemini.mrchecker.webapi.core.BasePageWebAPI;
import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.pages.environment.GetEnvironmentParam;

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

public class DeleteCookiesPage extends BasePageWebAPI {
	private final static String	HOSTNAME	= GetEnvironmentParam.HTTPBIN.getValue();
	private final static String	PATH		= "/cookies/delete";
	private final static String	ENDPOINT	= HOSTNAME + PATH;
	
	private String deleteCookiesEndpointWithValues;
	
	public Response deleteCookie(String name, String value) {
		deleteCookiesEndpointWithValues = ENDPOINT
				.concat("?")
				.concat(name)
				.concat("=")
				.concat(value);
		
		return DriverManager.getDriverWebAPI()
				.config(RestAssuredConfig.newConfig()
						.redirect(RedirectConfig.redirectConfig()
								.followRedirects(false)))
				.filter(CookieSession.getSession())
				.when()
				.get(deleteCookiesEndpointWithValues);
	}
	
	public String getEndpoint() {
		return deleteCookiesEndpointWithValues == null ? ENDPOINT : deleteCookiesEndpointWithValues;
	}
}
