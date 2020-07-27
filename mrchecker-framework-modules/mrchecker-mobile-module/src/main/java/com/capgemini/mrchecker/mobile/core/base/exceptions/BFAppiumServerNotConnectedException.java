package com.capgemini.mrchecker.mobile.core.base.exceptions;

import org.openqa.selenium.WebDriverException;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

public class BFAppiumServerNotConnectedException extends AssertionError {
	
	private static final long	serialVersionUID	= -1217616251968376336L;
	private static final String	EXCEPTION_MESSAGE	= "Unable to create connections to Appium Server\n";
	
	/**
	 * This exception should be thrown when connection to Appium Server is not possible
	 *
	 * @param e
	 */
	public BFAppiumServerNotConnectedException(WebDriverException e) {
		super(EXCEPTION_MESSAGE);
		BFLogger.logError(EXCEPTION_MESSAGE + e.toString());
	}
}
