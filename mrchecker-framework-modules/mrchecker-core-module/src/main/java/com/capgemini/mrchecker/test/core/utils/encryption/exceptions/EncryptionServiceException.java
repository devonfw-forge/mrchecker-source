package com.capgemini.mrchecker.test.core.utils.encryption.exceptions;

public class EncryptionServiceException extends RuntimeException {

    public EncryptionServiceException(Throwable cause) {
        super(cause.getMessage());
    }
}
