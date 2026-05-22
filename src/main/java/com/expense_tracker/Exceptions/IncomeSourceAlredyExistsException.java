package com.expense_tracker.Exceptions;

public class IncomeSourceAlredyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncomeSourceAlredyExistsException(String message) {
		super(message);

	}

}
