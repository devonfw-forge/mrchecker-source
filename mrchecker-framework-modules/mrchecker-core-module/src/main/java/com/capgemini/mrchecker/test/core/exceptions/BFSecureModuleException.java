package com.capgemini.mrchecker.test.core.exceptions;

public class BFSecureModuleException extends AssertionError {
	
	private static final long serialVersionUID = 6815162645071113994L;
	
	public BFSecureModuleException(String message) {
		super(generateExceptionMessage(message));
	}
	
	public BFSecureModuleException(String message, Throwable e) {
		super(generateExceptionMessage(message), e);
	}
	
	private static String generateExceptionMessage(String message) {
		return message == null ? "" : message;
	}
	
}
