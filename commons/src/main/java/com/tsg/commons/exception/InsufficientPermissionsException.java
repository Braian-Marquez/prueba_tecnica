package com.tsg.commons.exception;

public class InsufficientPermissionsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InsufficientPermissionsException(String message) {
		super(message);
	}
}
