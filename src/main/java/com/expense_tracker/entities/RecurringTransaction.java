package com.expense_tracker.entities;



import java.time.LocalDate;
import java.util.UUID;


import com.expense_tracker.enums.TransactionFrequency;
import com.expense_tracker.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class RecurringTransaction {
 
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType transactionType;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionFrequency transactionFrequency;
	
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private ExpenseCategory expenseCategory;
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private IncomeSource incomeSource;
	
	@Column(nullable = false)
	private double amount;

	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	private LocalDate startDate;
	
	@Column(nullable = false)
	private LocalDate nextDueDate;
	
	
	
	public RecurringTransaction() {
		super();
		
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionFrequency getTransactionFrequency() {
		return transactionFrequency;
	}

	public void setTransactionFrequency(TransactionFrequency transactionFrequency) {
		this.transactionFrequency = transactionFrequency;
	}

	public ExpenseCategory getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(ExpenseCategory expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	public IncomeSource getIncomeSource() {
		return incomeSource;
	}

	public void setIncomeSource(IncomeSource incomeSource) {
		this.incomeSource = incomeSource;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(LocalDate nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

	public UUID getId() {
		return id;
	}

	

	
	
	
	
	
	
}
