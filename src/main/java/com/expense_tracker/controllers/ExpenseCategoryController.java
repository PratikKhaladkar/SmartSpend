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
import com.expense_tracker.dtos.ExpenseCategoryResponseDto;

import com.expense_tracker.serviceRequirements.IExpenseCategoryService;

@RestController
@RequestMapping("/expense/category")
public class ExpenseCategoryController {

	private final IExpenseCategoryService expenseCategoryService;

	private final IToolsService toolsService;

	public ExpenseCategoryController(IExpenseCategoryService expenseCategoryService, IToolsService toolsService) {
		super();
		this.expenseCategoryService = expenseCategoryService;
		this.toolsService = toolsService;
	}

	@PostMapping("/create/{expenseCategory}")
	public ResponseEntity<?> createCustomExpenseCategory(@PathVariable String expenseCategory) {

		expenseCategoryService.createCustomExpenseCategory(expenseCategory);

		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@GetMapping("/getAll")
	public ResponseEntity<List<ExpenseCategoryResponseDto>> getAllExpenseCategories() {

		List<ExpenseCategoryResponseDto> response = toolsService.getAllExpenseCategories();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
