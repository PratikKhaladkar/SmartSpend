package com.expense_tracker.Exceptions;

public class InsufficientPasswordLengthException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientPasswordLengthException(String message) {
		super(message);

	}

}
