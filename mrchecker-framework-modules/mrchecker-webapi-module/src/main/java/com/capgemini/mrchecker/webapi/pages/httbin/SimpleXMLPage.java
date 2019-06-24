package com.capgemini.mrchecker.webapi.pages.httbin;

import com.capgemini.mrchecker.webapi.core.BasePageWebAPI;
import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.pages.environment.GetEnvironmentParam;

import io.restassured.response.Response;

public class SimpleXMLPage extends BasePageWebAPI {
	
	private final static String	HOSTNAME	= GetEnvironmentParam.HTTPBIN.getValue();
	private final static String	PATH		= "/xml";
	private final static String	ENDPOINT	= HOSTNAME + PATH;
	// description of XML "Page"
	private final static String	XML_DOCUMENT_ROOT		= "slideshow";
	private final static String	XML_ELEMENT_ITEM		= "item";
	private final static int	ITEMS_NUMBER			= 3;
	private final static String	XML_ELEMENT_DATE		= "date";
	private final static String	XML_ELEMENT_DATE_VALUE	= "Date of publication";
	private final static String	XML_ENCODING			= "us-ascii";
	
	public static String getXmlDocumentRoot() {
		return XML_DOCUMENT_ROOT;
	}
	
	public static int getItemsNumber() {
		return ITEMS_NUMBER;
	}
	
	public static String getXmlElementItem() {
		return XML_ELEMENT_ITEM;
	}
	
	public static String getXmlElementDate() {
		return XML_ELEMENT_DATE;
	}
	
	public static String getXmlElementDateValue() {
		return XML_ELEMENT_DATE_VALUE;
	}
	
	public static String getXmlEncoding() {
		return XML_ENCODING;
	}
	
	public Response getXMLDocument() {
		return DriverManager.getDriverWebAPI()
				.get(ENDPOINT);
	}
	
	@Override
	public String getEndpoint() {
		return ENDPOINT;
	}
}