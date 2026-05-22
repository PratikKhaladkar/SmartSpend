package com.expense_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expense_tracker.entities.RecurringTransaction;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface RecurringTransactionRepo extends JpaRepository<RecurringTransaction, UUID>{
    
	
	public List<RecurringTransaction> findByNextDueDateLessThanEqual(LocalDate nextDueDate);

	public List<RecurringTransaction> findAllByUserId(UUID id);
}
