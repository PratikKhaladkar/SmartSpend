package com.expense_tracker.ai.service;

import java.util.List;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.expense_tracker.dtos.TransactionResponseDto;

public class FilterResult {
	
    private List<TransactionResponseDto> transactions;
    private double total;

    public FilterResult(List<TransactionResponseDto> transactions) {
    	 this.transactions =
                 transactions == null
                 ? List.of()
                 : transactions;
        this.total = this.transactions.stream()
                .mapToDouble(TransactionResponseDto::getAmount)
                .sum();
        
    }

	public List<TransactionResponseDto> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionResponseDto> transactions) {
		this.transactions = transactions;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
    
}
