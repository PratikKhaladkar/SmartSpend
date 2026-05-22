package com.expense_tracker.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.expense_tracker.entities.Transaction;

@Repository
public interface TransactionRepo  extends JpaRepository<Transaction, UUID>,JpaSpecificationExecutor<Transaction>{

	  
	
	public List<Transaction>findAllByUserId(UUID id);
	
	
	
	
	

}
