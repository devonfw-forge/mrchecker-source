package com.capgemini.mrchecker.webapi.httpbin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.BasePageWebApiTest;
import com.capgemini.mrchecker.webapi.pages.httbin.SimpleXMLPage;
import com.capgemini.mrchecker.webapi.pages.httbin.SimpleXMLPage.XML_ATTRIBUTE;
import com.capgemini.mrchecker.webapi.pages.httbin.SimpleXMLPage.XML_ELEMENT;
import com.capgemini.mrchecker.webapi.pages.utils.SimpleXMLParser;

public class SimpleXMLTest extends BasePageWebApiTest {
	
	// data to check for in tests
	private final static XML_ELEMENT	ROOT_ELEMENT			= XML_ELEMENT.SLIDESHOW;
	private final static XML_ELEMENT	ITEM_ELEMENT			= XML_ELEMENT.ITEM;
	private final static XML_ATTRIBUTE	DATE_ATTRIBUTE			= XML_ATTRIBUTE.DATE;
	private final static String			XML_ENCODING			= "us-ascii";
	private final static String			DATE_ATTRIBUTE_VALUE	= "Date of publication";
	
	private static SimpleXMLPage	simpleXMLPage	= new SimpleXMLPage();
	private static Document			simpleXMLPageDocument;
	
	@BeforeClass
	public static void setup() {
		BFLogger.logDebug("Reading " + simpleXMLPage.getEndpoint());
		try {
			simpleXMLPageDocument = SimpleXMLParser.convertToDocumentNormalized(simpleXMLPage.getXMLDocument()
					.asInputStream());
		} catch (IOException e) {
			BFLogger.logInfo("Cannot convert and normalize XML document due to IO Exception. " + e.toString());
		} catch (SAXException e) {
			BFLogger.logInfo("Cannot convert XML document due to parsing problem. " + e.toString());
		}
		
	}
	
	@Test
	public void validateXMLPageRequestHTTPResponseCode() {
		final int HTTP_OK = 200;
		BFLogger.logInfo("Validating that request for " + simpleXMLPage.getEndpoint() + " completed with  status code: " + HTTP_OK);
		assertThat(simpleXMLPage.getXMLDocument()
				.getStatusCode(), equalTo(HTTP_OK));
	}
	
	@Test
	public void validateRootElementName() {
		BFLogger.logInfo("Validating that document root is " + ROOT_ELEMENT.getElementKey() + " element");
		assertThat(simpleXMLPageDocument.getDocumentElement()
				.getNodeName(), equalTo(ROOT_ELEMENT.getElementKey()));
	}
	
	@Test
	public void validateElementsNumberWhenSearchingByTag() {
		BFLogger.logInfo("Validating that " + ITEM_ELEMENT.getElementKey() + " element number is 3");
		NodeList itemsList = simpleXMLPageDocument.getElementsByTagName(ITEM_ELEMENT.getElementKey());
		assertThat(itemsList.getLength(), equalTo(3));
	}
	
	@Test
	public void validateRootElementAttributeValueAndNumberWhenSearchingByTag() {
		BFLogger.logInfo("Validating " + DATE_ATTRIBUTE.getAttributeKey() + " attribute value");
		NodeList itemsList = simpleXMLPageDocument.getElementsByTagName(ROOT_ELEMENT.getElementKey());
		// there can be only 1
		assertThat(itemsList.getLength(), equalTo(1));
		Element rootElement = (Element) (itemsList.item(0));
		
		assertThat(rootElement.getAttribute(DATE_ATTRIBUTE.getAttributeKey()), equalTo(DATE_ATTRIBUTE_VALUE));
	}
	
	@Test
	public void validateXmlDocumentEncoding() {
		BFLogger.logInfo("Validating document encoding  to be " + XML_ENCODING);
		assertThat(simpleXMLPageDocument.getXmlEncoding(), equalTo(XML_ENCODING));
	}
}
