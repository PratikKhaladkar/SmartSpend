package com.expense_tracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.expense_tracker.Exceptions.ForbiddenException;
import com.expense_tracker.Exceptions.InvalidRequestExecption;
import com.expense_tracker.Exceptions.TransactionNotFoundException;
import com.expense_tracker.dtos.TransactionDto;
import com.expense_tracker.entities.ExpenseCategory;
import com.expense_tracker.entities.IncomeSource;
import com.expense_tracker.entities.Transaction;
import com.expense_tracker.repositories.TransactionRepo;
import com.expense_tracker.security.service.SecurityUtil;
import com.expense_tracker.serviceRequirements.ITransactionService;

import jakarta.transaction.Transactional;

@Service
public class TransactionService implements ITransactionService {

	private final TransactionRepo transactionRepo;

	private final ServiceUtility serviceUtility;

	public TransactionService(TransactionRepo transactionRepo, ServiceUtility serviceUtility) {
		super();
		this.transactionRepo = transactionRepo;
		this.serviceUtility = serviceUtility;
	}

	@Override
	@Transactional
	public void createTransaction(TransactionDto transactionDto) {

		String strIncomeSource = transactionDto.getIncomeSource();
		String strExpenseCategory = transactionDto.getExpenseCategory();

		IncomeSource incomeSource = serviceUtility.getIncomeSourceFromString(strIncomeSource);
		ExpenseCategory expenseCategory = serviceUtility.getExpenseCategoryFromString(strExpenseCategory);

		if (serviceUtility.isValidTransaction(transactionDto.getTransactionType(), incomeSource, expenseCategory)) {

			if (transactionDto.getDate().isAfter(LocalDate.now())) {

				throw new IllegalArgumentException("Future transactions are not allowed");
			}
			Transaction transaction = new Transaction();

			transaction.setTransactionType(transactionDto.getTransactionType());
			transaction.setIncomeSource(incomeSource);

			transaction.setAmount(transactionDto.getAmount());

			transaction.setExpenseCategory(expenseCategory);

			transaction.setDate(transactionDto.getDate());
			transaction.setUser(SecurityUtil.getAuthenticatedUser());

			transactionRepo.save(transaction);

		}

	}

	

	@Override
	@Transactional
	public void editTrasaction(UUID tid, TransactionDto transactionDto) {

		Transaction transaction = transactionRepo.findById(tid)
				.orElseThrow(() -> new TransactionNotFoundException("Transaction wiht specified id not found"));

		if (!serviceUtility.isTransactionOwner(transaction)) {

			throw new ForbiddenException("editing other users transactions is Forbidden");
		}
		if (transaction.isRecurring()) {

			throw new InvalidRequestExecption("Recurring transaction can-not be edited");
		}

		String strIncomeSource = transactionDto.getIncomeSource();
		String strExpenseCategory = transactionDto.getExpenseCategory();

		IncomeSource incomeSource = serviceUtility.getIncomeSourceFromString(strIncomeSource);
		ExpenseCategory expenseCategory = serviceUtility.getExpenseCategoryFromString(strExpenseCategory);

		if (serviceUtility.isValidTransaction(transactionDto.getTransactionType(), incomeSource, expenseCategory)) {

			transaction.setAmount(transactionDto.getAmount());
			transaction.setDate(transactionDto.getDate());
			transaction.setExpenseCategory(expenseCategory);
			transaction.setIncomeSource(incomeSource);
			transaction.setTransactionType(transactionDto.getTransactionType());

			transactionRepo.save(transaction);

		}

	}

	@Override
	@Transactional
	public void deleteTrasaction(UUID tid) {

		Transaction transaction = transactionRepo.findById(tid)
				.orElseThrow(() -> new TransactionNotFoundException("Transaction with specified id is not found"));

		if (!serviceUtility.isTransactionOwner(transaction)) {

			throw new ForbiddenException("Deleting other users transactions is Forbidden");
		}
		if (transaction.isRecurring()) {

			throw new InvalidRequestExecption(
					"Recurring transaction can-not be deleted direclty ,you have to remove the corrosponding Recurring Subscription");
		}

		transactionRepo.delete(transaction);

	}

}
