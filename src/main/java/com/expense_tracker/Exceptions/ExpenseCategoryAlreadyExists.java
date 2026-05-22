package com.expense_tracker.Exceptions;

public class ExpenseCategoryAlreadyExists extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExpenseCategoryAlreadyExists(String message) {
		super(message);

	}

}
