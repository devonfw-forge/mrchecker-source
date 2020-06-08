package com.capgemini.mrchecker.webapi.core;

import java.io.IOException;
import java.util.Objects;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.core.base.driver.DriverManager;
import com.capgemini.mrchecker.webapi.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.webapi.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.webapi.core.soap.SoapMessageGenerator;
import com.google.inject.Guice;
import com.jamesmurty.utils.XMLBuilder;

abstract public class BasePageWebAPI extends Page implements IWebAPI {
	
	private static DriverManager driver = null;
	
	private final static PropertiesFileSettings	propertiesFileSettings;
	private final static IAnalytics				ANALYTICS;
	public final static String					ANALYTICS_CATEGORY_NAME	= "WebAPI-Module";
	
	static {
		// Get analytics instance created in BaseTets
		ANALYTICS = BaseTest.getAnalytics();
		
		// Get and then set properties information from selenium.settings file
		propertiesFileSettings = setPropertiesSettings();
		
		// Read System or maven parameters
		setRuntimeParametersWebApi();
		
		// Read Environment variables either from environmnets.csv or any other input data.
		setEnvironmetInstance();
	}
	
	public static IAnalytics getAnalytics() {
		return ANALYTICS;
	}
	
	public BasePageWebAPI() {
		getDriver();
	}
	
	@Override
	public ModuleType getModuleType() {
		return ModuleType.WEBAPI;
	}
	
	public static DriverManager getDriver() {
		if (Objects.isNull(BasePageWebAPI.driver)) {
			// Create module driver
			BasePageWebAPI.driver = new DriverManager(propertiesFileSettings);
		}
		return BasePageWebAPI.driver;
	}
	
	private static PropertiesFileSettings setPropertiesSettings() {
		// Get and then set properties information from settings.properties file
		return Guice.createInjector(PropertiesSettingsModule.init())
				.getInstance(PropertiesFileSettings.class);
	}
	
	private static void setRuntimeParametersWebApi() {
		// Read System or maven parameters
		BFLogger.logDebug(java.util.Arrays.asList(RuntimeParameters.values())
				.toString());
		
	}
	
	private static void setEnvironmetInstance() {
		/*
		 * Environment variables either from environmnets.csv or any other input data. For now there is no properties
		 * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
		 * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
		 */
		
	}
	
	public static class SOAPTemplate {
		
		private XMLBuilder xmlBody;
		
		/*
		 * SOAP response built from Java code
		 */
		public SOAPTemplate(String root) {
			setRoot(root);
		}
		
		/**
		 * @return Generate SOAP request in String format
		 */
		public String getMessage() {
			String message = "";
			try {
				SOAPMessage soapMessage = SoapMessageGenerator.createSOAPmessage(this.getRoot()
						.asString());
				message = SoapMessageGenerator.printSoapMessage(soapMessage);
			} catch (SOAPException | SAXException | IOException | ParserConfigurationException | TransformerException e) {
				// TODO: refactor that
				new Exception(e);
			}
			return message;
		}
		
		/**
		 * @return Root XML structure
		 */
		public XMLBuilder getRoot() {
			return xmlBody;
		}
		
		/*
		 * ----------------------------------
		 * Any handy actions after this point
		 * ----------------------------------
		 */
		private void setRoot(String nodeName) {
			try {
				this.xmlBody = XMLBuilder.create(nodeName);
			} catch (ParserConfigurationException | FactoryConfigurationError e) {
				// TODO: refactor that
				new Exception(e);
			}
		}
		
		/*
		 * Set up an attribute for root
		 */
		public void addAttributeToRoot(String name, String value) {
			this.xmlBody.attribute(name, value);
		}
	}
	
}
