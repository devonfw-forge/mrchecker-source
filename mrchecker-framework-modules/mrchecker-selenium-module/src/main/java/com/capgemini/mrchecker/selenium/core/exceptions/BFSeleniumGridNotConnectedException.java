package com.capgemini.mrchecker.selenium.core.exceptions;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.openqa.selenium.WebDriverException;

public class BFSeleniumGridNotConnectedException extends AssertionError {

    private static final long serialVersionUID = -1217616251968376336L;
    private static String exceptionMessage;

    /**
     * This exception should be thrown when connection to Selenium Grid is not possible
     *
     * @param e e
     */
    public BFSeleniumGridNotConnectedException(WebDriverException e) {
        super(generateExceptionMessage(), e);
        BFLogger.logError(exceptionMessage + e.toString());
    }

    private static String generateExceptionMessage() {
        exceptionMessage = "Unable to create connections to selenium grid\n";
        return exceptionMessage;
    }
}
