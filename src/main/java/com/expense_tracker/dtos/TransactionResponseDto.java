package com.expense_tracker.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.expense_tracker.entities.Transaction;

public class TransactionResponseDto {

	private UUID tid;
	private double amount;

	private String transactionType;

	private String incomeSource;

	private String expenseCategory;

	private LocalDate date;

	private boolean isRecurring;

	public TransactionResponseDto(Transaction transaction) {
		super();
		this.tid = transaction.getId();
		this.amount = transaction.getAmount();
		this.transactionType = transaction.getTransactionType().name();
		if (transaction.getIncomeSource() != null) {
			this.incomeSource = transaction.getIncomeSource().getName();
		} else {
			this.incomeSource = null;
		}
		if (transaction.getExpenseCategory() != null) {
			this.expenseCategory = transaction.getExpenseCategory().getName();
		} else {
			this.expenseCategory = null;
		}
		this.date = transaction.getDate();
		this.isRecurring = transaction.isRecurring();
	}

	public UUID getTid() {
		return tid;
	}

	public void setTid(UUID tid) {
		this.tid = tid;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getIncomeSource() {
		return incomeSource;
	}

	public void setIncomeSource(String incomeSource) {
		this.incomeSource = incomeSource;
	}

	public String getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

}
