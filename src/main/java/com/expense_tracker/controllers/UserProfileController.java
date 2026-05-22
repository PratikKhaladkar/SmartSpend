package com.expense_tracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.dtos.ApiResponseDto;
import com.expense_tracker.dtos.ChangePasswordRequestDto;
import com.expense_tracker.dtos.ProfileResponseDto;
import com.expense_tracker.serviceRequirements.IUserProfileService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

	private final IUserProfileService userProfileService;

	public UserProfileController(IUserProfileService userProfileService) {
		super();
		this.userProfileService = userProfileService;

	}

	@GetMapping("/get")
	public ResponseEntity<ProfileResponseDto> getUseProfile() {

		ProfileResponseDto response = userProfileService.getUserProfile();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/change/password")
	public ResponseEntity<ApiResponseDto> changePasword(
			@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {

		ApiResponseDto response = userProfileService.changePassword(changePasswordRequestDto.getCurrentPassword(),
				changePasswordRequestDto.getNewPassword());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
