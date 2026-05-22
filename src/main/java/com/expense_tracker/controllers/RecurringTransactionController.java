package com.expense_tracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.PatchExchange;

import com.expense_tracker.ai.ServiceRequrements.IToolsService;
import com.expense_tracker.dtos.RecurringTransactionDto;
import com.expense_tracker.dtos.RecurringTransactionResponseDto;
import com.expense_tracker.entities.RecurringTransaction;
import com.expense_tracker.serviceRequirements.IRecurringTransactionService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/recurring/transaction")
public class RecurringTransactionController {
   
  private final	IRecurringTransactionService recurringTransactionService;
	private final IToolsService toolsService;
	

	
     
   
	 public RecurringTransactionController(IRecurringTransactionService recurringTransactionService,
			IToolsService toolsService) {
		super();
		this.recurringTransactionService = recurringTransactionService;
		this.toolsService = toolsService;
	}

	 @PostMapping("/create")
	public ResponseEntity<?> createRecurringTransaction(@Valid @RequestBody RecurringTransactionDto recurringTransactionDto) {
		    
		 recurringTransactionService.createRecurringTransaction(recurringTransactionDto);
		 
		 return new ResponseEntity<>(HttpStatus.CREATED);
	}
    
	 @DeleteMapping("/delete/{tid}")
	public ResponseEntity<?> deleteRecurringTransaction (@PathVariable UUID tid) {
		  
		recurringTransactionService.deleteRecurringTransaction(tid);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
  
	 @PatchMapping("/edit/{tid}/{newAmount}")
	public ResponseEntity<?> editAmountOfRecurringTransaction(@PathVariable UUID tid,@PathVariable double newAmount) {
		
		recurringTransactionService.editAmountOfRecurringTransaction(tid, newAmount);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
		
	}
	
	 @GetMapping("/getAll")
	public ResponseEntity<List<RecurringTransactionResponseDto>> getAllRecurringTransactions(){
		   
		
		  List<RecurringTransactionResponseDto> response = toolsService.getAllRecurringTransactions();
		  
		  return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
