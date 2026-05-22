package com.expense_tracker.serviceRequirements;

import com.expense_tracker.dtos.ApiResponseDto;
import com.expense_tracker.dtos.ProfileResponseDto;

public interface IUserProfileService {

	ProfileResponseDto getUserProfile();

	ApiResponseDto changePassword(String currentPassword, String newPassword);

}
