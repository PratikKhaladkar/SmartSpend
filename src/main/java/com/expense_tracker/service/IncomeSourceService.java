package com.expense_tracker.service;



import java.util.UUID;

import org.springframework.stereotype.Service;

import com.expense_tracker.Exceptions.IncomeSourceAlredyExistsException;
import com.expense_tracker.Exceptions.IncomeSurceNotFoundException;
import com.expense_tracker.Exceptions.InvalidRequestExecption;
import com.expense_tracker.entities.IncomeSource;
import com.expense_tracker.repositories.IncomeSourceRepo;
import com.expense_tracker.security.service.SecurityUtil;
import com.expense_tracker.serviceRequirements.IIncomeSourceService;

import jakarta.transaction.Transactional;

@Service
public class IncomeSourceService implements IIncomeSourceService {

	private final IncomeSourceRepo incomeSourceRepo;

	public IncomeSourceService(IncomeSourceRepo incomeSourceRepo) {
		super();
		this.incomeSourceRepo = incomeSourceRepo;
	}

	@Override
	@Transactional
	public void createCustomIncomeSource(String incomesource) {

		if (incomesource == null || incomesource.length() == 0) {

			throw new InvalidRequestExecption("Income Source can-not be null or Blank");
		}
		
		incomesource = incomesource.toUpperCase();
		
		if (!incomeSourceRepo.isExistsByNameAndBelongsToDefaultOrUser(incomesource,
				SecurityUtil.getAuthenticatedUser().getId())) {
			incomeSourceRepo.save(new IncomeSource(incomesource, SecurityUtil.getAuthenticatedUser()));
		} else {

			throw new IncomeSourceAlredyExistsException("Income Source: " + incomesource + " alredy exists");
		}

	}

	@Override
	@Transactional
	public void editCustomIncomeSource(String currentIncomesource, String newIncomesource) {

		UUID userId = SecurityUtil.getAuthenticatedUser().getId();

		if (currentIncomesource == null || currentIncomesource.length() == 0 || newIncomesource == null
				|| newIncomesource.length() == 0) {

			throw new InvalidRequestExecption("Income Source can-not be null or Blank");
		}

		currentIncomesource = currentIncomesource.toUpperCase();
		newIncomesource = newIncomesource.toUpperCase();

		if (incomeSourceRepo.isBelongsToSystem(currentIncomesource)) {

			throw new InvalidRequestExecption(
					"can-not edit default Income Sources, can edit only custom created sources");
		}

		if (incomeSourceRepo.isExistsByNameAndBelongsToDefaultOrUser(newIncomesource, userId)) {

			throw new IncomeSourceAlredyExistsException("Income Source:" + newIncomesource + " alredy exists");
		}

		if (!incomeSourceRepo.isBelongsToUser(currentIncomesource, userId)) {

			throw new IncomeSurceNotFoundException("You don't have Income source named:" + currentIncomesource);
		}

		IncomeSource incomsourceFromdDb = incomeSourceRepo.findByNameAndBelongsTOUser(currentIncomesource, userId)
				.orElseThrow();

		incomsourceFromdDb.setName(newIncomesource);

		incomeSourceRepo.save(incomsourceFromdDb);

	}

}
