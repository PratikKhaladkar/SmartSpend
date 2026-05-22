package com.expense_tracker.dtos;

import java.time.LocalDate;

import com.expense_tracker.entities.Transaction;
import com.expense_tracker.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionDto {

	@NotNull(message = "Amount required")
	private double amount;

	@NotNull(message = "TransactionType Required")
	private TransactionType transactionType;

	private String incomeSource;

	private String expenseCategory;

	@NotNull(message = "Date Required")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate date;

	public TransactionDto() {
		super();

	}

	public TransactionDto(@NotNull double amount, @NotNull TransactionType transactionType, @NotNull LocalDate date) {
		super();
		this.amount = amount;
		this.transactionType = transactionType;
		this.date = date;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
