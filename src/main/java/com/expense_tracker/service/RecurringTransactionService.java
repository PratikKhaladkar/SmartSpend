package com.expense_tracker.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.expense_tracker.Exceptions.ForbiddenException;
import com.expense_tracker.Exceptions.TransactionNotFoundException;
import com.expense_tracker.dtos.RecurringTransactionDto;
import com.expense_tracker.entities.ExpenseCategory;
import com.expense_tracker.entities.IncomeSource;
import com.expense_tracker.entities.RecurringTransaction;
import com.expense_tracker.entities.Transaction;
import com.expense_tracker.enums.TransactionFrequency;
import com.expense_tracker.repositories.RecurringTransactionRepo;
import com.expense_tracker.repositories.TransactionRepo;
import com.expense_tracker.security.service.SecurityUtil;
import com.expense_tracker.serviceRequirements.IRecurringTransactionService;

import jakarta.transaction.Transactional;

@Service
public class RecurringTransactionService implements IRecurringTransactionService {

	private final ServiceUtility serviceUtility;

	private final RecurringTransactionRepo recurringTransactionRepo;

	private final TransactionRepo transactionRepo;


	public RecurringTransactionService(ServiceUtility serviceUtility, RecurringTransactionRepo recurringTransactionRepo,
			TransactionRepo transactionRepo) {
		super();
		this.serviceUtility = serviceUtility;
		this.recurringTransactionRepo = recurringTransactionRepo;
		this.transactionRepo = transactionRepo;
	}

	@Override
	@Transactional
	public void createRecurringTransaction(RecurringTransactionDto rtransactionDto) {

		String strIncomeSource = rtransactionDto.getIncomeSource();
		String strExpenseCategory = rtransactionDto.getExpenseCategory();

		IncomeSource incomeSource = serviceUtility.getIncomeSourceFromString(strIncomeSource);
		ExpenseCategory expenseCategory = serviceUtility.getExpenseCategoryFromString(strExpenseCategory);

		if (serviceUtility.isValidTransaction(rtransactionDto.getTransactionType(), incomeSource,
				expenseCategory)) {

			RecurringTransaction recurringTransaction = new RecurringTransaction();

			recurringTransaction.setAmount(rtransactionDto.getAmount());
			recurringTransaction.setExpenseCategory(expenseCategory);
            recurringTransaction.setIncomeSource(incomeSource);
			recurringTransaction.setTransactionFrequency(rtransactionDto.getTransactionFrequency());
			recurringTransaction.setTransactionType(rtransactionDto.getTransactionType());

			int billingDay = rtransactionDto.getBillingDay();

			LocalDate today = LocalDate.now();
			LocalDate billingdate;

			TransactionFrequency transactionFrequency = rtransactionDto.getTransactionFrequency();

			if (transactionFrequency.equals(TransactionFrequency.MONTHLY)) {

				if (billingDay < 1 || billingDay > 31) {

					throw new IllegalArgumentException("Billing day must lie between 1 and 31");
				}

				billingdate = today.withDayOfMonth(Math.min(billingDay, today.lengthOfMonth()));

			} else if (transactionFrequency.equals(TransactionFrequency.WEEKLY)) {

				if (billingDay < 1 || billingDay > 7) {

					throw new IllegalArgumentException("Billing day must lie between 1 and 7");
				}

				billingdate = today.with(DayOfWeek.of(billingDay));

			} else {

				billingdate = today;
			}

			if (billingdate.isBefore(today) || billingdate.isEqual(today)) {

				Transaction transaction = new Transaction();

				transaction.setTransactionType(rtransactionDto.getTransactionType());
				transaction.setIncomeSource(incomeSource);

				transaction.setAmount(rtransactionDto.getAmount());

				transaction.setExpenseCategory(expenseCategory);

				transaction.setDate(billingdate);
				transaction.setUser(SecurityUtil.getAuthenticatedUser());

				transaction.setRecurring(true);

				transactionRepo.save(transaction);
			}

			if (billingdate.isAfter(today)) {

				recurringTransaction.setNextDueDate(billingdate);
			} else {

				if (transactionFrequency.equals(TransactionFrequency.MONTHLY)) {

					recurringTransaction.setNextDueDate(billingdate.plusMonths(1));
				}

				else if (transactionFrequency.equals(TransactionFrequency.WEEKLY)) {

					recurringTransaction.setNextDueDate(billingdate.plusWeeks(1));
				} else {

					recurringTransaction.setNextDueDate(billingdate.plusDays(1));
				}

			}

			recurringTransaction.setStartDate(billingdate);

			recurringTransaction.setUser(SecurityUtil.getAuthenticatedUser());

			recurringTransactionRepo.save(recurringTransaction);

		}

	}

	@Override
	@Transactional
	public void deleteRecurringTransaction(UUID tid) {
		  
		RecurringTransaction rTransaction = recurringTransactionRepo.findById(tid).orElseThrow(()->new TransactionNotFoundException("Recurring Transaction wiht specified id not found"));
		
		if (!serviceUtility.isRecurringTransactionOwner(rTransaction)) {
		 
			throw new ForbiddenException("Deleting other users Recurring Transactions is Forbidden");
		}
		  
		recurringTransactionRepo.delete(rTransaction);
	}

	@Override
	@Transactional
	public void editAmountOfRecurringTransaction(UUID tid,double newAmount) {
		 
		
		  RecurringTransaction recurringTransaction = recurringTransactionRepo.findById(tid).orElseThrow(()->new TransactionNotFoundException("Recurring transaction with specified id not found"));
		   
		  if (!serviceUtility.isRecurringTransactionOwner(recurringTransaction)) {
				 
				throw new ForbiddenException("editing other users Recurring Transactions is Forbidden");
			}
		  
		    
		  recurringTransaction.setAmount(newAmount);
		  
		  recurringTransactionRepo.save(recurringTransaction);
		    
	}


	
}
