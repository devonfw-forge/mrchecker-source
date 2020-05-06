package com.capgemini.mrchecker.mobile.core.base.exceptions;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.openqa.selenium.WebDriverException;

public class BFAppiumServerNotConnectedException extends AssertionError {

	private static final long   serialVersionUID = -1217616251968376336L;
	private static       String exceptionMessage;

	/**
	 * This exception should be thrown when connection to Appium Server is not possible
	 *
	 * @param e
	 */
	public BFAppiumServerNotConnectedException(WebDriverException e) {
		super(generateExceptionMessage());
		BFLogger.logError(exceptionMessage + e.toString());
	}

	private static String generateExceptionMessage() {
		exceptionMessage = "Unable to create connections to Appium Server\n";
		return exceptionMessage;
	}
}
