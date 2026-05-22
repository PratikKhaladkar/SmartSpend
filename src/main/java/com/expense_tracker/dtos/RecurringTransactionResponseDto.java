package com.expense_tracker.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.expense_tracker.entities.RecurringTransaction;

public class RecurringTransactionResponseDto {
   
	private UUID tId;
	
	private String transactionType;

	private String transactionFrequency;

	private String expenseCategory;

	private String incomeSource;

	private double amount;

	private LocalDate nextDueDate;

	public RecurringTransactionResponseDto(RecurringTransaction recurringTransaction) {
		super();
		this.tId=recurringTransaction.getId();
		this.amount = recurringTransaction.getAmount();
		this.transactionType = recurringTransaction.getTransactionType().name();
		
		if (recurringTransaction.getIncomeSource() != null) {
			this.incomeSource = recurringTransaction.getIncomeSource().getName();
		} else {
			this.incomeSource = null;
		}
		if (recurringTransaction.getExpenseCategory() != null) {
			this.expenseCategory = recurringTransaction.getExpenseCategory().getName();
		} else {
			this.expenseCategory = null;
		}
		this.transactionFrequency = recurringTransaction.getTransactionFrequency().name();
		this.nextDueDate = recurringTransaction.getNextDueDate();
	}

	public UUID gettId() {
		return tId;
	}

	public void settId(UUID tId) {
		this.tId = tId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionFrequency() {
		return transactionFrequency;
	}

	public void setTransactionFrequency(String transactionFrequency) {
		this.transactionFrequency = transactionFrequency;
	}

	public String getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	public String getIncomeSource() {
		return incomeSource;
	}

	public void setIncomeSource(String incomeSource) {
		this.incomeSource = incomeSource;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(LocalDate nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

}
