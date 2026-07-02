package com.expense_tracker.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.expense_tracker.Exceptions.ExpenseCatogoryNotFoundException;
import com.expense_tracker.Exceptions.IncomeSurceNotFoundException;
import com.expense_tracker.entities.ExpenseCategory;
import com.expense_tracker.entities.IncomeSource;
import com.expense_tracker.entities.RecurringTransaction;
import com.expense_tracker.entities.Transaction;
import com.expense_tracker.enums.TransactionType;
import com.expense_tracker.repositories.ExpenseCategoryRepo;
import com.expense_tracker.repositories.IncomeSourceRepo;
import com.expense_tracker.repositories.RecurringTransactionRepo;
import com.expense_tracker.repositories.TransactionRepo;
import com.expense_tracker.security.service.SecurityUtil;

@Service
public class ServiceUtility {

	private final RecurringTransactionRepo recurringTransactionRepo;
	private final TransactionRepo transactionRepo;

	private final IncomeSourceRepo incomeSourceRepo;
	private final ExpenseCategoryRepo expenseCategoryRepo;

	public ServiceUtility(RecurringTransactionRepo recurringTransactionRepo, TransactionRepo transactionRepo,
			IncomeSourceRepo incomeSourceRepo, ExpenseCategoryRepo expenseCategoryRepo) {
		super();
		this.recurringTransactionRepo = recurringTransactionRepo;
		this.transactionRepo = transactionRepo;
		this.incomeSourceRepo = incomeSourceRepo;
		this.expenseCategoryRepo = expenseCategoryRepo;
	}

	public boolean isValidTransaction(TransactionType transactionType, IncomeSource incomeSource,
			ExpenseCategory expenseCategory) {

		if (transactionType.equals(TransactionType.EXPENSE)) {

			if (expenseCategory == null) {

				throw new IllegalArgumentException("Expense-category can-not be null");
			}
		} else {

			if (incomeSource == null) {

				throw new IllegalArgumentException("Income Source can-not be null");
			}
		}

		return true;
	}

	public boolean isTransactionOwner(Transaction transaction) {

		UUID id = SecurityUtil.getAuthenticatedUser().getId();

		List<Transaction> transactions = transactionRepo.findAllByUserId(id);

		return transactions.contains(transaction);
	}

	public boolean isRecurringTransactionOwner(RecurringTransaction recurringTransaction) {

		UUID id = SecurityUtil.getAuthenticatedUser().getId();

		List<RecurringTransaction> recurringTransactions = recurringTransactionRepo.findAllByUserId(id);

		return recurringTransactions.contains(recurringTransaction);
	}

	public IncomeSource getIncomeSourceFromString(String strIncomeSource) {

		IncomeSource incomeSource;
		UUID userId = SecurityUtil.getAuthenticatedUser().getId();

		if (strIncomeSource != null) {

			if (!incomeSourceRepo.isExistsByNameAndBelongsToDefaultOrUser(strIncomeSource, userId)) {

				throw new IncomeSurceNotFoundException("Specified IncomSource not found ,but you have facility to create custom incomesource,you can use the same to create this one");
			}

			incomeSource = incomeSourceRepo.findByNameAndBelongsToDefaultOrUser(strIncomeSource,userId).orElseThrow();

		} else {
			incomeSource = null;
		}

		return incomeSource;

	}

	public ExpenseCategory getExpenseCategoryFromString(String strExpenseCategory) {

		ExpenseCategory expenseCategory;
		UUID userId = SecurityUtil.getAuthenticatedUser().getId();

		if (strExpenseCategory != null) {

			if (!expenseCategoryRepo.isExistsByNameAndBelongsToDefaultOrUser(strExpenseCategory, userId)) {

				throw new ExpenseCatogoryNotFoundException("Specified expense category not found,but you have facility to create custom expense category,you can use the same to create this one");
			}

			expenseCategory = expenseCategoryRepo.findByNameAndBelongsToDefaultOrUser(strExpenseCategory,userId).orElseThrow();
		} else {
			expenseCategory = null;
		}

		return expenseCategory;
	}
	


}
