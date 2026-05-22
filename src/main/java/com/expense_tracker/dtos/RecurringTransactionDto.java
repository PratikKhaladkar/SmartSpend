package com.expense_tracker.dtos;


import com.expense_tracker.enums.TransactionFrequency;
import com.expense_tracker.enums.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecurringTransactionDto {

	@NotNull(message = "Amount required")
	private double amount;

	@NotNull(message = "Transaction Type Required")
	private TransactionType transactionType;

	private String incomeSource;

	private String expenseCategory;

	@NotNull(message = "Transaction Frequency Required")
	private TransactionFrequency transactionFrequency;

	private int billingDay;

	public RecurringTransactionDto() {
		super();

	}

	public RecurringTransactionDto(@NotNull double amount, @NotNull TransactionType transactionType,
			@NotNull TransactionFrequency transactionFrequency, int billingDay) {
		super();
		this.amount = amount;
		this.transactionType = transactionType;

		this.transactionFrequency = transactionFrequency;
		this.billingDay = billingDay;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public String getIncomeSource() {
		return incomeSource;
	}

	public void setIncomeSource(String incomeSource) {

		if (incomeSource != null) {
			this.incomeSource = incomeSource.toUpperCase();
		} else {
			this.incomeSource = null;
		}
	}

	public void setExpenseCategory(String expenseCategory) {

		if (expenseCategory != null) {
			this.expenseCategory = expenseCategory.toUpperCase();
		} else {
			this.expenseCategory = null;
		}
	}

	public String getExpenseCategory() {
		return expenseCategory;
	}

	public int getBillingDay() {
		return billingDay;
	}

	public void setBillingDay(int billingDay) {
		this.billingDay = billingDay;
	}

	public TransactionFrequency getTransactionFrequency() {
		return transactionFrequency;
	}

	public void setTransactionFrequency(TransactionFrequency transactionFrequency) {
		this.transactionFrequency = transactionFrequency;
	}

}
