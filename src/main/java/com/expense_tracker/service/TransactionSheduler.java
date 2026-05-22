package com.expense_tracker.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.expense_tracker.entities.RecurringTransaction;
import com.expense_tracker.entities.Transaction;
import com.expense_tracker.enums.TransactionFrequency;
import com.expense_tracker.repositories.RecurringTransactionRepo;
import com.expense_tracker.repositories.TransactionRepo;
import com.expense_tracker.security.service.SecurityUtil;

import jakarta.transaction.Transactional;

@Service
public class TransactionSheduler {

	private final RecurringTransactionRepo recurringTransactionRepo;

	private final TransactionRepo transactionRepo;

	public TransactionSheduler(RecurringTransactionRepo recurringTransactionRepo, TransactionRepo transactionRepo) {
		super();
		this.recurringTransactionRepo = recurringTransactionRepo;
		this.transactionRepo = transactionRepo;
	}

	@Transactional
	@Scheduled(cron = "0 0 1 * * *")
	public void sheduledJob() {

		LocalDate today = LocalDate.now();

		List<RecurringTransaction> dueTransactions = recurringTransactionRepo.findByNextDueDateLessThanEqual(today);

		for (RecurringTransaction rTransaction : dueTransactions) {

			LocalDate currentDueDate = rTransaction.getNextDueDate();

			TransactionFrequency transactionFrequency = rTransaction.getTransactionFrequency();

			Transaction transaction = new Transaction();

			transaction.setDate(currentDueDate);
			transaction.setAmount(rTransaction.getAmount());

			transaction.setTransactionType(rTransaction.getTransactionType());
			transaction.setExpenseCategory(rTransaction.getExpenseCategory());

			transaction.setIncomeSource(rTransaction.getIncomeSource());

			transaction.setRecurring(true);

			transaction.setUser(SecurityUtil.getAuthenticatedUser());

			transactionRepo.save(transaction);

			if (transactionFrequency.equals(TransactionFrequency.MONTHLY)) {

				rTransaction.setNextDueDate(currentDueDate.plusMonths(1));
			}

			else if (transactionFrequency.equals(TransactionFrequency.WEEKLY)) {

				rTransaction.setNextDueDate(currentDueDate.plusWeeks(1));
			} else {

				rTransaction.setNextDueDate(currentDueDate.plusDays(1));
			}

			recurringTransactionRepo.save(rTransaction);

		}

	}
}
