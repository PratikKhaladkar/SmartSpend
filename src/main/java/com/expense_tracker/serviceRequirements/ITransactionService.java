package com.expense_tracker.serviceRequirements;

import java.util.List;
import java.util.UUID;


import com.expense_tracker.dtos.TransactionDto;
import com.expense_tracker.entities.Transaction;


public interface ITransactionService {
    
	
	
	void createTransaction(TransactionDto transactionDto);

	

	void editTrasaction(UUID id, TransactionDto transactionDto);

	void deleteTrasaction(UUID id);
	
   

	
}
