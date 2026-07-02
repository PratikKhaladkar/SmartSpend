package com.expense_tracker.controllers;


import com.expense_tracker.serviceRequirements.ITransactionService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.ai.ServiceRequrements.IToolsService;
import com.expense_tracker.ai.service.FilterResult;
import com.expense_tracker.dtos.FilterTransactionDto;
import com.expense_tracker.dtos.TransactionDto;
import com.expense_tracker.dtos.TransactionResponseDto;
import com.expense_tracker.entities.Transaction;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	private final ITransactionService transactionService;
	
	private final IToolsService toolsService;
	
	

	public TransactionController(ITransactionService transactionService, IToolsService toolsService) {
		super();
		this.transactionService = transactionService;
		this.toolsService = toolsService;
	}

	@PostMapping("/create")
	public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {

		transactionService.createTransaction(transactionDto);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {

		FilterResult filterResult = toolsService.getAllTransactions();
   
		 List<TransactionResponseDto> response = filterResult.getTransactions();
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
    
	@PutMapping("/edit/{tid}")
	public ResponseEntity<?> editTrasaction( @Valid @RequestBody TransactionDto transactionDto,@PathVariable UUID tid) {

		transactionService.editTrasaction(tid, transactionDto);

		return new ResponseEntity<>(HttpStatus.OK);
	}
   
	@DeleteMapping("/delete/{tid}")
	public ResponseEntity<?> deleteTrasaction(@PathVariable UUID tid) {

		transactionService.deleteTrasaction(tid);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/filter")
	public ResponseEntity<List<TransactionResponseDto>> filterTransactions(@RequestBody FilterTransactionDto filterTransactionDto) {

		   FilterResult filterResult = toolsService.filterTransactions(filterTransactionDto);
		   
		   List<TransactionResponseDto> response = filterResult.getTransactions();
		   return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	

}
