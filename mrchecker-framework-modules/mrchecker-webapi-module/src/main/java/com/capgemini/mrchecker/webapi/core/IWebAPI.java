package com.capgemini.mrchecker.webapi.core;

import com.capgemini.mrchecker.test.core.IPage;

public interface IWebAPI extends IPage {
	
	/**
	 * @return URL Endpoint in String format
	 */
	String getEndpoint();
}
