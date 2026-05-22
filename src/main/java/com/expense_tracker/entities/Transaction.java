package com.expense_tracker.entities;

import java.time.LocalDate;

import java.util.UUID;

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
public class Transaction {
    
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
    
	@Column(nullable = false)
	private double amount;
    
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType transactionType;
	
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private IncomeSource incomeSource;
	
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private ExpenseCategory expenseCategory;

	private LocalDate date;
	 
     
	@ManyToOne
	@JoinColumn(name="user_id" ,nullable = false)
	private User user;
	
	@Column(nullable = false)
	private boolean isRecurring;
    
	

	public Transaction() {
		super();
		
	}

	public UUID getId() {
		return id;
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

	public IncomeSource getIncomeSource() {
		return incomeSource;
	}

	public void setIncomeSource(IncomeSource incomeSource) {
		this.incomeSource = incomeSource;
	}

	public ExpenseCategory getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(ExpenseCategory expenseCategory) {
		this.expenseCategory = expenseCategory;
	}



	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}
	
	
	
	
	
	
	
}
