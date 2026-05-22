package com.expense_tracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {
    
	@Email(message = "Invalid Email")
	private String email;
    
	@NotBlank(message = "Password required")
	private String password;

	public LoginRequestDto() {
		super();

	}

	public LoginRequestDto(@Email String email, @NotBlank String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
