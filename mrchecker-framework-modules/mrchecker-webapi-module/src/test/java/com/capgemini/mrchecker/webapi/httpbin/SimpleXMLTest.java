package com.capgemini.mrchecker.webapi.httpbin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.BasePageWebApiTest;
import com.capgemini.mrchecker.webapi.pages.httbin.SimpleXMLPage;

public class SimpleXMLTest extends BasePageWebApiTest {
	
	private static SimpleXMLPage simpleXMLPage ;
	private static Document simpleXMLPageDocument;

	@BeforeClass
	public static void setup(){
		simpleXMLPage = new SimpleXMLPage();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		BFLogger.logDebug("Reading " + simpleXMLPage.getEndpoint());
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			simpleXMLPageDocument = dBuilder.parse(simpleXMLPage.getXMLDocument().asInputStream());
			simpleXMLPageDocument.normalizeDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void validateXMLPageRequestHTTPResponseCode() {
		final int HTTP_OK = 200;
		BFLogger.logInfo("Validating that request for " + simpleXMLPage.getEndpoint() + " completed with  status code: " + HTTP_OK);
		assertThat(HTTP_OK,equalTo(simpleXMLPage.getXMLDocument().getStatusCode()));

	}
	@Test
	public void validateRootElementName() {
		BFLogger.logInfo("Validating that document root is " + simpleXMLPage.getXmlDocumentRoot());
		assertThat(simpleXMLPage.getXmlDocumentRoot(),equalTo(simpleXMLPageDocument.getDocumentElement().getNodeName()));
	}

	@Test
	public void validateElementsNumberWhenSearchingByTag() {
		BFLogger.logInfo("Validating that item element number is  " + simpleXMLPage.getItemsNumber());
		NodeList itemsList = simpleXMLPageDocument.getElementsByTagName(simpleXMLPage.getXmlElementItem());
		assertThat(simpleXMLPage.getItemsNumber(),equalTo(itemsList.getLength()));
	}

	@Test
	public void validateRootElementAttributeValueAndNumberWhenSearchingByTag() {
		BFLogger.logInfo("Validating attribute value  " + simpleXMLPage.getXmlElementDate());
		NodeList itemsList = simpleXMLPageDocument.getElementsByTagName(simpleXMLPage.getXmlDocumentRoot());
		//there can be only 1
		assertThat(itemsList.getLength(),equalTo(1));
		Element rootElement = (Element)(itemsList.item(0));

		assertThat(simpleXMLPage.getXmlElementDateValue(),equalTo(rootElement.getAttribute(simpleXMLPage.getXmlElementDate())));
	}

	@Test
	public void validateXmlDocumentEncoding() {
		BFLogger.logInfo("Validating document encoding  to be " + simpleXMLPageDocument.getXmlEncoding());
		assertThat(simpleXMLPageDocument.getXmlEncoding(),equalTo(simpleXMLPageDocument.getXmlEncoding()));
	}
}
