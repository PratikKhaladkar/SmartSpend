package com.expense_tracker.dtos;

import com.expense_tracker.entities.ExpenseCategory;

public class ExpenseCategoryResponseDto {
  
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public ExpenseCategoryResponseDto(ExpenseCategory expenseCategory) {
		super();

		if (expenseCategory != null) {
			this.name = expenseCategory.getName();
		} else {
			this.name = null;
		}
		
	}
	
	
}
