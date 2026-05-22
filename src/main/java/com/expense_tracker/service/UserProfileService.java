package com.expense_tracker.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.expense_tracker.Exceptions.InsufficientPasswordLengthException;
import com.expense_tracker.dtos.ApiResponseDto;
import com.expense_tracker.dtos.ProfileResponseDto;
import com.expense_tracker.entities.User;
import com.expense_tracker.repositories.UserRepo;
import com.expense_tracker.security.service.SecurityUtil;
import com.expense_tracker.serviceRequirements.IUserProfileService;
import com.google.genai.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class UserProfileService implements IUserProfileService {
    
	private final UserRepo userRepo;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public UserProfileService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userRepo = userRepo;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public ProfileResponseDto getUserProfile() {
		 
		User user=SecurityUtil.getAuthenticatedUser();
		return new ProfileResponseDto(user.getName(),user.getEmail());
	}

	@Override
	@Transactional
	public ApiResponseDto changePassword(String currentPassword,String newPassword) {
		 
		User user = userRepo.findById(SecurityUtil.getAuthenticatedUser().getId()).orElseThrow();
		
	
		  if(!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
			  
			  throw new BadCredentialsException("Incorrect current password");
		  }
		  
		  if(newPassword.length()<6) {
			  
			  throw new InsufficientPasswordLengthException("Password length must be at least 6 or more");
		  }
		  
		  
		  user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		  
		  userRepo.save(user);
		  
		
		
		return new ApiResponseDto("UPDATED", "Password changed SucessFully");
	}

}
