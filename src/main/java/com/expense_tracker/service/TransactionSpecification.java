package com.expense_tracker.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.expense_tracker.entities.ExpenseCategory;
import com.expense_tracker.entities.IncomeSource;
import com.expense_tracker.entities.Transaction;
import com.expense_tracker.enums.TransactionType;


public class TransactionSpecification {
     
	
	public static Specification<Transaction>hasUserId(UUID userid){
		  
		return (root,query,cb) ->
        cb.equal(root.get("user").get("id"), userid);
	}
	
	public static Specification<Transaction>hasTransactionType(TransactionType transactionType){
		
		return (root,query,cb)->transactionType==null?null:cb.equal(root.get("transactionType"), transactionType);
	}
	
	public static Specification<Transaction>hasExpenseCategory(ExpenseCategory expenseCategory){
		
		return (root,query,cb)->expenseCategory==null?null:cb.equal(root.get("expenseCategory"), expenseCategory);
	}
	
	public static Specification<Transaction>hasIncomeSource(IncomeSource incomeSource){
		
		return (root,query,cb)->incomeSource==null?null:cb.equal(root.get("incomeSource"), incomeSource);
	}
	
	public static Specification<Transaction>hasDateRange(LocalDate from,LocalDate to){
		
		return (root, query, cb) -> {

	        if (from != null && to != null) {
	            return cb.between(root.get("date"), from, to);
	        }

	        if (from != null) {
	            return cb.greaterThanOrEqualTo(root.get("date"), from);
	        }

	        if (to != null) {
	            return cb.lessThanOrEqualTo(root.get("date"), to);
	        }

	        return null;
	    };
	}
}
