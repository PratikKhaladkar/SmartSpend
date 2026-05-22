package com.expense_tracker.dtos;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordRequestDto {

	@NotBlank(message = "Current password Required")
	private String currentPassword;
	@NotBlank(message = "New password Required")
	private String newPassword;

	public ChangePasswordRequestDto() {
		super();

	}

	public ChangePasswordRequestDto(@NotBlank(message = "Current password Required") String currentPassword,
			@NotBlank(message = "New password Required") String newPassword) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


}
