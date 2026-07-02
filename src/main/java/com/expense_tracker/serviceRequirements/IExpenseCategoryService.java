package com.expense_tracker.serviceRequirements;

import java.util.List;

import com.expense_tracker.entities.ExpenseCategory;

public interface IExpenseCategoryService  {
    
	   
	void createCustomExpenseCategory(String expenseCategory);
	
	void editCustomExpenseCategory(String currentExpenseCategory,String newExpenseCategory);
	
}
