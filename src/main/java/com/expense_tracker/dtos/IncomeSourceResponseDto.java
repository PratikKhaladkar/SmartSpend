package com.expense_tracker.dtos;

import com.expense_tracker.entities.IncomeSource;

public class IncomeSourceResponseDto {
    
	private String name;

	
	public IncomeSourceResponseDto(IncomeSource incomeSource) {
		super();
		if (incomeSource != null) {
			this.name = incomeSource.getName();
		} else {
			this.name = null;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
