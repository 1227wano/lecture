package com.kh.secom.exception;

public class MismatchPasswordException extends RuntimeException {

	public MismatchPasswordException(String message) {
		super(message);
	}
}
