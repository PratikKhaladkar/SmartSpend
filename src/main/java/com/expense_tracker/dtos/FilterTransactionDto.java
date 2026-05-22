package com.expense_tracker.dtos;

import java.time.LocalDate;

import com.expense_tracker.enums.TransactionType;

public class FilterTransactionDto {

	private TransactionType transactionType;
	private String incomeSource;
	private String expenseCategory;
	private LocalDate from;
	private LocalDate to;
	private Integer lastNMonths;
    private Integer lastNWeeks;
    private Integer lastNDays;
    private Integer lastNYears;
    private Integer fromMonth;  // 1-12
    private Integer fromYear;
    private Integer toMonth;    // 1-12
    private Integer toYear;
	public FilterTransactionDto() {
		super();

	}

	public FilterTransactionDto(TransactionType transactionType, LocalDate from, LocalDate to) {
		super();
		this.transactionType = transactionType;

		this.from = from;
		this.to = to;
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
			;
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

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}

	public Integer getLastNMonths() {
		return lastNMonths;
	}

	public void setLastNMonths(Integer lastNMonths) {
		this.lastNMonths = lastNMonths;
	}

	public Integer getLastNWeeks() {
		return lastNWeeks;
	}

	public void setLastNWeeks(Integer lastNWeeks) {
		this.lastNWeeks = lastNWeeks;
	}

	public Integer getLastNDays() {
		return lastNDays;
	}

	public void setLastNDays(Integer lastNDays) {
		this.lastNDays = lastNDays;
	}

	public Integer getLastNYears() {
		return lastNYears;
	}

	public void setLastNYears(Integer lastNYears) {
		this.lastNYears = lastNYears;
	}

	public Integer getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(Integer fromMonth) {
		this.fromMonth = fromMonth;
	}

	public Integer getFromYear() {
		return fromYear;
	}

	public void setFromYear(Integer fromYear) {
		this.fromYear = fromYear;
	}

	public Integer getToMonth() {
		return toMonth;
	}

	public void setToMonth(Integer toMonth) {
		this.toMonth = toMonth;
	}

	public Integer getToYear() {
		return toYear;
	}

	public void setToYear(Integer toYear) {
		this.toYear = toYear;
	}
	
	

}
