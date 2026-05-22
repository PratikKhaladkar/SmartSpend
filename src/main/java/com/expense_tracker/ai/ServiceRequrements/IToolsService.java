package com.expense_tracker.ai.ServiceRequrements;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import com.expense_tracker.ai.service.FilterResult;
import com.expense_tracker.dtos.ExpenseCategoryResponseDto;
import com.expense_tracker.dtos.FilterTransactionDto;
import com.expense_tracker.dtos.IncomeSourceResponseDto;
import com.expense_tracker.dtos.RecurringTransactionResponseDto;

import com.expense_tracker.dtos.TransactionResponseDto;

public interface IToolsService {
	
    
	FilterResult filterTransactions(FilterTransactionDto filterTransactionDto);
	
	
	FilterResult getAllTransactions();
	List<ExpenseCategoryResponseDto>getAllExpenseCategories();
	List<IncomeSourceResponseDto>getAllIncomeSources();
	List<RecurringTransactionResponseDto>getAllRecurringTransactions();
	
	LocalDate getCurrentDateTime();

}
