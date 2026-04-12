package com.expense_tracker.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.expense_tracker.auth.requirements.AuthRequirements;
import com.expense_tracker.dtos.RegistrationDto;
import com.expense_tracker.entities.User;
import com.expense_tracker.repositories.UserRepo;

@Service
public class AuthService implements AuthRequirements {
  
	 private UserRepo userRepo;
	 
	 private BCryptPasswordEncoder passwordEncoder;
	 
	@Autowired
	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Autowired
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}




	@Override
	public void register(RegistrationDto registrationDto) {

		String encodedpassword = passwordEncoder.encode(registrationDto.getPassword());

		userRepo.save(new User(registrationDto.getName(), registrationDto.getEmail(), encodedpassword));

	}

}
