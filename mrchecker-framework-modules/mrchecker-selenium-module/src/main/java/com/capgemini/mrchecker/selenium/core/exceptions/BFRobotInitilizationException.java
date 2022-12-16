package com.capgemini.mrchecker.selenium.core.exceptions;

import java.awt.*;
import java.util.Arrays;

public class BFRobotInitilizationException extends RuntimeException {

    public BFRobotInitilizationException(AWTException e) {
        System.err.println("Unable to execute Robot action. \n" + Arrays.toString(e.getStackTrace()));
    }
}
