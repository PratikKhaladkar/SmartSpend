package com.expense_tracker.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.expense_tracker.entities.User;
import com.expense_tracker.repositories.UserRepo;


@Service
public class AppUserDetailsService implements UserDetailsService {

	private UserRepo userRepo;
	 
	@Autowired
	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		  
		 User user = userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Invalid email"));
		 
		 return new AppUserDetails(user);
		
	}

}
