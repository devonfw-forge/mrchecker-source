package com.capgemini.mrchecker.cli.core.exceptions;

public class BFCliException extends RuntimeException {
	
	public BFCliException() {
		super();
	}
	
	public BFCliException(String message) {
		super(message);
	}
	
	public BFCliException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BFCliException(Throwable cause) {
		super(cause);
	}
	
}
