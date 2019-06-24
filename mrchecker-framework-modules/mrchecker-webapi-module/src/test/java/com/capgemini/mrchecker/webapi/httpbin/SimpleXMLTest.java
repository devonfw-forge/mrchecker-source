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
import com.capgemini.mrchecker.webapi.pages.utils.SimpleXMLParser;

public class SimpleXMLTest extends BasePageWebApiTest {
	
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
		assertThat(HTTP_OK, equalTo(simpleXMLPage.getXMLDocument()
				.getStatusCode()));
		
	}
	
	@Test
	public void validateRootElementName() {
		BFLogger.logInfo("Validating that document root is " + simpleXMLPage.getXmlDocumentRoot());
		assertThat(simpleXMLPage.getXmlDocumentRoot(), equalTo(simpleXMLPageDocument.getDocumentElement()
				.getNodeName()));
	}
	
	@Test
	public void validateElementsNumberWhenSearchingByTag() {
		BFLogger.logInfo("Validating that item element number is  " + simpleXMLPage.getItemsNumber());
		NodeList itemsList = simpleXMLPageDocument.getElementsByTagName(simpleXMLPage.getXmlElementItem());
		assertThat(simpleXMLPage.getItemsNumber(), equalTo(itemsList.getLength()));
	}
	
	@Test
	public void validateRootElementAttributeValueAndNumberWhenSearchingByTag() {
		BFLogger.logInfo("Validating attribute value  " + simpleXMLPage.getXmlElementDate());
		NodeList itemsList = simpleXMLPageDocument.getElementsByTagName(simpleXMLPage.getXmlDocumentRoot());
		// there can be only 1
		assertThat(itemsList.getLength(), equalTo(1));
		Element rootElement = (Element) (itemsList.item(0));
		
		assertThat(simpleXMLPage.getXmlElementDateValue(), equalTo(rootElement.getAttribute(simpleXMLPage.getXmlElementDate())));
	}
	
	@Test
	public void validateXmlDocumentEncoding() {
		BFLogger.logInfo("Validating document encoding  to be " + simpleXMLPageDocument.getXmlEncoding());
		assertThat(simpleXMLPageDocument.getXmlEncoding(), equalTo(simpleXMLPageDocument.getXmlEncoding()));
	}
}
