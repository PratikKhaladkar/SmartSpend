package com.expense_tracker.auth.requirements;

import com.expense_tracker.dtos.RegistrationDto;

public interface AuthRequirements {
     
	void register(RegistrationDto registrationDto);
}
