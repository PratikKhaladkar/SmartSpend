package com.expense_tracker.serviceRequirements;

import java.util.List;
import java.util.UUID;

import com.expense_tracker.dtos.RecurringTransactionDto;
import com.expense_tracker.entities.RecurringTransaction;

public interface IRecurringTransactionService {
	
	
	void createRecurringTransaction(RecurringTransactionDto recurringTransactionDto);

	void deleteRecurringTransaction(UUID id);

	void editAmountOfRecurringTransaction(UUID id, double newamount);
	
	
}
