package com.capgemini.mrchecker.webapi.unit.core.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

/**
 * This is a helper class allowing for transformations of various XML documents representations
 */
public class SimpleXMLParser {
	private static DocumentBuilderFactory	dbFactory;
	private static DocumentBuilder			dBuilder;
	
	static {
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			BFLogger.logInfo("Cannot properly configure Document Parser." + e.toString());
		}
	}
	
	/**
	 * Transforms XML Document inputStream representation to Document object. Returns normalized Document.
	 * 
	 * @param inputStream
	 *            - input stream XML document representation
	 * @return - normalized Document representation of XML
	 * @throws IOException
	 *             IOException
	 * @throws SAXException
	 *             SAXException
	 */
	public static Document convertToDocumentNormalized(InputStream inputStream) throws IOException, SAXException {
		Document simpleXMLPageDocument = dBuilder.parse(inputStream);
		simpleXMLPageDocument.normalizeDocument();
		return simpleXMLPageDocument;
	}
}
