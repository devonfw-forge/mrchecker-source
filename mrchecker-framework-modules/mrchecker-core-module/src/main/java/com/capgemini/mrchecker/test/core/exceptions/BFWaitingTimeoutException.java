package com.capgemini.mrchecker.test.core.exceptions;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.DurationUtils;

import java.time.Duration;

public class BFWaitingTimeoutException extends AssertionError {

    private static final long serialVersionUID = 6762218637002539008L;
    private static String exceptionMessage;

    /**
     * This error should be thrown when time specified by {@code timeout} parameter has passed while waiting for given
     * component. Example: ProgressBar that gets displayed after clicking on Account in AccountSelector is loading
     * longer than 30 seconds. Calling "throw new BFWaitingTimeoutException("BorderedProgressBar", 30)" is going to
     * create error message: "Timed out waiting [30] seconds for [BorderedProgressBar] to load."
     *
     * @param componentName componentName
     * @param timeout       timeout
     */
    public BFWaitingTimeoutException(String componentName, Duration timeout) {
        super(generateExceptionMessage(componentName, timeout));
        BFLogger.logError(exceptionMessage);
    }

    private static String generateExceptionMessage(String componentName, Duration timeout) {
        exceptionMessage = "Timed out waiting [" + DurationUtils.getReadableDuration(timeout) + "] for [" + componentName + "] to load.";
        return exceptionMessage;
    }
}
