package com.capgemini.mrchecker.jemmy.exceptions;

public class JemmyException extends RuntimeException {
	public JemmyException() {
		super();
	}

	public JemmyException(String message) {
		super(message);
	}

	public JemmyException(String message, Throwable cause) {
		super(message, cause);
	}

	public JemmyException(Throwable cause) {
		super(cause);
	}

}
