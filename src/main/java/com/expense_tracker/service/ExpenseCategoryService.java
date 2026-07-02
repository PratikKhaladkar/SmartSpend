package com.expense_tracker.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.expense_tracker.Exceptions.ExpenseCategoryAlreadyExists;
import com.expense_tracker.Exceptions.ExpenseCatogoryNotFoundException;
import com.expense_tracker.Exceptions.InvalidRequestExecption;
import com.expense_tracker.entities.ExpenseCategory;
import com.expense_tracker.entities.User;
import com.expense_tracker.repositories.ExpenseCategoryRepo;
import com.expense_tracker.security.service.SecurityUtil;
import com.expense_tracker.serviceRequirements.IExpenseCategoryService;

import jakarta.transaction.Transactional;

@Service
public class ExpenseCategoryService implements IExpenseCategoryService {

	private final ExpenseCategoryRepo expenseCategoryRepo;

	public ExpenseCategoryService(ExpenseCategoryRepo expenseCategoryRepo) {
		super();
		this.expenseCategoryRepo = expenseCategoryRepo;
	}

	@Override
	@Transactional
	public void createCustomExpenseCategory(String expenseCategory) {

		if (expenseCategory == null || expenseCategory.length() == 0) {

			throw new InvalidRequestExecption("Expense category can-not be null or Blank");
		}
		
		expenseCategory = expenseCategory.toUpperCase();
		
		if (!expenseCategoryRepo.isExistsByNameAndBelongsToDefaultOrUser(expenseCategory,
				SecurityUtil.getAuthenticatedUser().getId())) {

			expenseCategoryRepo.save(new ExpenseCategory(expenseCategory, SecurityUtil.getAuthenticatedUser()));

		} else {
			throw new ExpenseCategoryAlreadyExists("Expense category: " + expenseCategory + " already exists");

		}

	}

	@Override
	@Transactional
	public void editCustomExpenseCategory(String currentExpenseCategory, String newExpenseCategory) {

		UUID userid = SecurityUtil.getAuthenticatedUser().getId();

		if (currentExpenseCategory == null || currentExpenseCategory.length() == 0 || newExpenseCategory == null
				|| newExpenseCategory.length() == 0) {

			throw new InvalidRequestExecption("Expense category can-not be null or Blank");
		}

		currentExpenseCategory = currentExpenseCategory.toUpperCase();
		newExpenseCategory = newExpenseCategory.toUpperCase();

		if (expenseCategoryRepo.isBelongsToSystem(currentExpenseCategory)) {

			throw new InvalidRequestExecption(
					"can-not edit deault expesnse category,can edit only you custom created categories");
		}

		if (expenseCategoryRepo.isExistsByNameAndBelongsToDefaultOrUser(newExpenseCategory, userid)) {

			throw new ExpenseCategoryAlreadyExists("Expense category:" + newExpenseCategory + " already exists");
		}

		if (!expenseCategoryRepo.isBelongsToUser(currentExpenseCategory, userid)) {

			throw new ExpenseCatogoryNotFoundException("You have no expenseCategory named:" + currentExpenseCategory);

		}

		ExpenseCategory expenseCategoryfromdb = expenseCategoryRepo
				.findByNameAndBelongsToUser(currentExpenseCategory, userid).orElseThrow();

		expenseCategoryfromdb.setName(newExpenseCategory);

		expenseCategoryRepo.save(expenseCategoryfromdb);

	}


	
}
