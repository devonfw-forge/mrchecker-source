package com.capgemini.mrchecker.test.core.exceptions;

public class BFStaticVariableException extends RuntimeException {
    private static final String MESSAGE_FORMAT = "Mr Checker does not support static %s variables";

    public BFStaticVariableException(String objectName) {
        super(String.format(MESSAGE_FORMAT, objectName));
    }
}