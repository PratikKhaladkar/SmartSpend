package com.expense_tracker.controllers;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.ai.ServiceRequrements.IToolsService;
import com.expense_tracker.dtos.IncomeSourceResponseDto;

import com.expense_tracker.serviceRequirements.IIncomeSourceService;

@RestController
@RequestMapping("/income/source")
public class IncomSourceController {

	private final IIncomeSourceService incomeSourceService;
	private final IToolsService toolsService;
   
	

	public IncomSourceController(IIncomeSourceService incomeSourceService, IToolsService toolsService) {
		super();
		this.incomeSourceService = incomeSourceService;
		this.toolsService = toolsService;
	}

	@PostMapping("/create/{incomesource}")
	public ResponseEntity<?> createCustomIncomeSource(@PathVariable String incomesource) {

		incomeSourceService.createCustomIncomeSource(incomesource);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	

	@GetMapping("/getAll")
	public ResponseEntity<List<IncomeSourceResponseDto>> getAllIncomeSources() {

		List<IncomeSourceResponseDto> response = toolsService.getAllIncomeSources();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
