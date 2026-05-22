package com.expense_tracker.ai.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import com.expense_tracker.Exceptions.ExpenseCatogoryNotFoundException;
import com.expense_tracker.Exceptions.IncomeSurceNotFoundException;
import com.expense_tracker.ai.ServiceRequrements.IToolsService;
import com.expense_tracker.dtos.ExpenseCategoryResponseDto;
import com.expense_tracker.dtos.FilterTransactionDto;
import com.expense_tracker.dtos.IncomeSourceResponseDto;
import com.expense_tracker.dtos.RecurringTransactionResponseDto;
import com.expense_tracker.dtos.TransactionDto;
import com.expense_tracker.dtos.TransactionResponseDto;
import com.expense_tracker.entities.ExpenseCategory;
import com.expense_tracker.entities.IncomeSource;
import com.expense_tracker.entities.RecurringTransaction;
import com.expense_tracker.entities.Transaction;
import com.expense_tracker.repositories.ExpenseCategoryRepo;
import com.expense_tracker.repositories.IncomeSourceRepo;
import com.expense_tracker.repositories.RecurringTransactionRepo;
import com.expense_tracker.repositories.TransactionRepo;
import com.expense_tracker.security.service.SecurityUtil;
import com.expense_tracker.service.TransactionSpecification;

@Service
public class Tools implements IToolsService {

	private final TransactionRepo transactionRepo;

	private final ExpenseCategoryRepo expenseCategoryRepo;

	private final IncomeSourceRepo incomeSourceRepo;

	private final RecurringTransactionRepo recurringTransactionRepo;

	public Tools(TransactionRepo transactionRepo, ExpenseCategoryRepo expenseCategoryRepo,
			IncomeSourceRepo incomeSourceRepo, RecurringTransactionRepo recurringTransactionRepo) {
		super();
		this.transactionRepo = transactionRepo;

		this.expenseCategoryRepo = expenseCategoryRepo;
		this.incomeSourceRepo = incomeSourceRepo;
		this.recurringTransactionRepo = recurringTransactionRepo;

	}

	@Override
	@Tool(description = """
		    Filters the authenticated user's transactions based on optional criteria.
		    All fields in FilterTransactionDto are optional — pass null to skip that filter.
		    Use getCurrentDateTime before setting any date fields.
		    Returns FilterResult with a filtered transaction list and pre-calculated total — use total directly, do not re-sum.
		""")
	public FilterResult filterTransactions(FilterTransactionDto filterTransactionDto) {
     
		
	    
		 LocalDate today = LocalDate.now();

		 if (filterTransactionDto.getLastNDays() != null) {
			    int n = filterTransactionDto.getLastNDays();
			    if (n == 0) {
			        // today
			        filterTransactionDto.setFrom(today);
			        filterTransactionDto.setTo(today);
			    } else if (n == 1) {
			        // yesterday → rolling = same as previous 1 day
			        filterTransactionDto.setFrom(today.minusDays(1));
			        filterTransactionDto.setTo(today.minusDays(1));
			    } else if (n > 1) {
			        // last N days → rolling window
			        filterTransactionDto.setFrom(today.minusDays(n));
			        filterTransactionDto.setTo(today);
			    } else {
			        // previous N days → exact completed days
			        // -1 = yesterday, -2 = day before yesterday
			        int abs = Math.abs(n);
			        filterTransactionDto.setFrom(today.minusDays(abs));
			        filterTransactionDto.setTo(today.minusDays(1));
			    }
			} else if (filterTransactionDto.getLastNWeeks() != null) {
		     int n = filterTransactionDto.getLastNWeeks();
		     if (n == 0) {
		         // this week → Monday of current week to today
		         filterTransactionDto.setFrom(today.with(DayOfWeek.MONDAY));
		         filterTransactionDto.setTo(today);
		     } else if (n > 0) {
		         // last N weeks → rolling window of past N*7 days
		         filterTransactionDto.setFrom(today.minusWeeks(n));
		         filterTransactionDto.setTo(today);
		     } else {
		         // previous N weeks → exact completed weeks
		         // -1 = previous 1 completed week (last Mon to last Sun)
		         int abs = Math.abs(n);
		         filterTransactionDto.setFrom(today.minusWeeks(abs).with(DayOfWeek.MONDAY));
		         filterTransactionDto.setTo(today.minusWeeks(1).with(DayOfWeek.SUNDAY));
		     }

		 } else if (filterTransactionDto.getLastNMonths() != null) {
		     int n = filterTransactionDto.getLastNMonths();
		     if (n == 0) {
		         // this month → 1st of current month to today
		         filterTransactionDto.setFrom(today.withDayOfMonth(1));
		         filterTransactionDto.setTo(today);
		     } else if (n > 0) {
		         // last N months → rolling window of past N months from today
		         filterTransactionDto.setFrom(today.minusMonths(n));
		         filterTransactionDto.setTo(today);
		     } else {
		         // previous N months → exact completed months
		         // -1 = previous 1 completed month (1st to last day of last month)
		         // -2 = previous 2 completed months (1st of 2 months ago to last day of last month)
		         int abs = Math.abs(n);
		         LocalDate startMonth = today.minusMonths(abs);
		         LocalDate endMonth = today.minusMonths(1);
		         filterTransactionDto.setFrom(startMonth.withDayOfMonth(1));
		         filterTransactionDto.setTo(endMonth.withDayOfMonth(endMonth.lengthOfMonth()));
		     }

		 } else if (filterTransactionDto.getLastNYears() != null) {
		     int n = filterTransactionDto.getLastNYears();
		     if (n == 0) {
		         // this year → Jan 1st of current year to today
		         filterTransactionDto.setFrom(today.withDayOfYear(1));
		         filterTransactionDto.setTo(today);
		     } else if (n > 0) {
		         // last N years → rolling window of past N years from today
		         filterTransactionDto.setFrom(today.minusYears(n));
		         filterTransactionDto.setTo(today);
		     } else {
		         // previous N years → exact completed years
		         // -1 = previous 1 completed year (Jan 1 to Dec 31 of last year)
		         // -2 = previous 2 completed years (Jan 1 of 2 years ago to Dec 31 of last year)
		         int abs = Math.abs(n);
		         filterTransactionDto.setFrom(today.minusYears(abs).withDayOfYear(1));
		         filterTransactionDto.setTo(today.minusYears(1).withMonth(12).withDayOfMonth(31));
		     }
		 }

		 // Explicit month/year handling
		 if (filterTransactionDto.getFromMonth() != null && filterTransactionDto.getFromYear() != null) {
		     filterTransactionDto.setFrom(
		         LocalDate.of(filterTransactionDto.getFromYear(), filterTransactionDto.getFromMonth(), 1)
		     );
		 }
		 if (filterTransactionDto.getToMonth() != null && filterTransactionDto.getToYear() != null) {
		     LocalDate toDate = LocalDate.of(filterTransactionDto.getToYear(), filterTransactionDto.getToMonth(), 1);
		     filterTransactionDto.setTo(toDate.withDayOfMonth(toDate.lengthOfMonth()));
		 }

		 // Fallback: fromMonth without fromYear → use current year
		 if (filterTransactionDto.getFromMonth() != null && filterTransactionDto.getFromYear() == null) {
		     filterTransactionDto.setFrom(
		         LocalDate.of(today.getYear(), filterTransactionDto.getFromMonth(), 1)
		     );
		 }

		 // Fallback: toMonth without toYear → use current year
		 if (filterTransactionDto.getToMonth() != null && filterTransactionDto.getToYear() == null) {
		     LocalDate toDate = LocalDate.of(today.getYear(), filterTransactionDto.getToMonth(), 1);
		     filterTransactionDto.setTo(toDate.withDayOfMonth(toDate.lengthOfMonth()));
		 }

		 // Fallback: if to is set but from is still null → single day query
		 if (filterTransactionDto.getFrom() == null && filterTransactionDto.getTo() != null) {
		     filterTransactionDto.setFrom(filterTransactionDto.getTo());
		 }

	    
		IncomeSource incomeSource = null;
		if (filterTransactionDto.getIncomeSource() != null) {

			if (incomeSourceRepo.isExistsByNameAndBelongsToDefaultOrUser(filterTransactionDto.getIncomeSource(),
					SecurityUtil.getAuthenticatedUser().getId())) {

				incomeSource = incomeSourceRepo.findByNameAndBelongsToDefaultOrUser(filterTransactionDto.getIncomeSource(),SecurityUtil.getAuthenticatedUser().getId()).orElseThrow();
			}else {
				
				throw new IncomeSurceNotFoundException("Specified IncomSource not found ,but you have facility to create custom incomesource,you can use the same to create this one");
			}
		}

		ExpenseCategory expenseCategory = null;
		if (filterTransactionDto.getExpenseCategory() != null) {

			if (expenseCategoryRepo.isExistsByNameAndBelongsToDefaultOrUser(filterTransactionDto.getExpenseCategory(),
					SecurityUtil.getAuthenticatedUser().getId())) {

				expenseCategory = expenseCategoryRepo.findByNameAndBelongsToDefaultOrUser(filterTransactionDto.getExpenseCategory(),SecurityUtil.getAuthenticatedUser().getId())
						.orElseThrow();
			}
			else {
				
				throw new ExpenseCatogoryNotFoundException("Specified expense category not found,but you have facility to create custom expense category,you can use the same to create this one");
			}
			
		}

		UUID userId = SecurityUtil.getAuthenticatedUser().getId();

		Specification<Transaction> spec =

				Specification.allOf(TransactionSpecification.hasUserId(userId)
						.and(TransactionSpecification.hasTransactionType(filterTransactionDto.getTransactionType()))
						.and(TransactionSpecification.hasExpenseCategory(expenseCategory))
						.and(TransactionSpecification.hasIncomeSource(incomeSource)).and(TransactionSpecification
								.hasDateRange(filterTransactionDto.getFrom(), filterTransactionDto.getTo())));

		List<Transaction> transactions = transactionRepo.findAll(spec);

		List<TransactionResponseDto> result = transactions.stream().map((transaction) -> new TransactionResponseDto(transaction)).toList();
		
		return new FilterResult(result);
	}

	

	@Override
	@Tool(description =  """
	        Returns ALL transactions of the authenticated user with no filters applied.
	        Returns a FilterResult containing:
	        - transactions: complete list of TransactionResponseDto
	        - total: the sum of all transaction amounts (use this directly, no need to sum manually)
	        """)
	public FilterResult getAllTransactions() {

		List<Transaction> transactions = transactionRepo.findAllByUserId(SecurityUtil.getAuthenticatedUser().getId());
	List<TransactionResponseDto> result = transactions.stream().map((transaction) -> new TransactionResponseDto(transaction)).toList();
	   
	 return new FilterResult(result);
	
	}

	@Override
	@Tool(description = """
			Returns all expense categories belonging to the authenticated user.
			Always call this before filtering by expense category to validate the category name exists.
			Returns a List of ExpenseCategoryResponseDto objects each containing a 'name' field.
			""")
	public List<ExpenseCategoryResponseDto> getAllExpenseCategories() {

		List<ExpenseCategory> expenseCategories = expenseCategoryRepo
				.getAllExpesExpenseCategories(SecurityUtil.getAuthenticatedUser().getId());

		return expenseCategories.stream().map((expensecategory) -> new ExpenseCategoryResponseDto(expensecategory))
				.toList();
	}

	@Override
	@Tool(description = """
	        Returns all income sources belonging to the authenticated user.
	        Always call this before filtering by income source to validate the source name exists.
	        Returns a List of IncomeSourceResponseDto objects each containing a 'name' field.
	        """)
	public List<IncomeSourceResponseDto> getAllIncomeSources() {

		List<IncomeSource> incomeSources = incomeSourceRepo
				.getAllIncomeSources(SecurityUtil.getAuthenticatedUser().getId());

		return incomeSources.stream().map((incomsource) -> new IncomeSourceResponseDto(incomsource)).toList();
	}

	@Override
	@Tool(description = """
			Returns all recurring transactions (subscriptions and scheduled payments)
			of the authenticated user.
			Returns a List of RecurringTransactionResponseDto objects containing
			transactionType, transactionFrequency, expenseCategory, incomeSource, amount, and nextDueDate.
			""")
	public List<RecurringTransactionResponseDto> getAllRecurringTransactions() {

		List<RecurringTransaction> recurringTransactions = recurringTransactionRepo
				.findAllByUserId(SecurityUtil.getAuthenticatedUser().getId());

		return recurringTransactions.stream()
				.map((recurringTransaction) -> new RecurringTransactionResponseDto(recurringTransaction)).toList();
	}

	@Override
	@Tool(description = """
			    Returns the current date and time.
			    Use this when the user asks about today's date, current time, current day, month, or year.
			""")
	public LocalDate getCurrentDateTime() {
		return LocalDate.now();
	}

}
