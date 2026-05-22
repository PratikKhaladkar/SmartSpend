package com.expense_tracker.controllers;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.auth.requirements.AuthRequirements;
import com.expense_tracker.dtos.ApiResponseDto;
import com.expense_tracker.dtos.LoginRequestDto;
import com.expense_tracker.dtos.RegistrationDto;
import com.expense_tracker.entities.RefreshToken;
import com.expense_tracker.security.jwt.JwtFilterUtility;
import com.expense_tracker.security.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private JwtFilterUtility jwtFilterUtility;

	private AuthenticationManager authenticationManager;

	private RefreshTokenService refreshTokenService;
	
	private AuthRequirements authService;
	
	@Value("${app.cookie.secure:false}")
	private boolean secureCookie;

    
	@Autowired
	public void setAuthService(AuthRequirements authService) {
		this.authService = authService;
	}

	@Autowired
	public void setJwtFilterUtility(JwtFilterUtility jwtFilterUtility) {
		this.jwtFilterUtility = jwtFilterUtility;
	}

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Autowired
	public void setRefreshTokenService(RefreshTokenService refreshTokenService) {
		this.refreshTokenService = refreshTokenService;
	}
     
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponseDto>register(@Valid @RequestBody RegistrationDto registrationDto){
		  
		
		authService.register(registrationDto);
		
		return new ResponseEntity<>(new ApiResponseDto("201 CREATED","Registered Successfully"),HttpStatus.CREATED);
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletRequest request) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

		ResponseCookie accessCookie = jwtFilterUtility.generateToken(authentication,request);

		ResponseCookie refreshCookie = refreshTokenService.generateRefreshToken(authentication, request);

	

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
				.body(new ApiResponseDto("200 OK","Logged in Successfully"));
	}

	@PostMapping("/refresh")
	public ResponseEntity<?>tokenRefresh(HttpServletRequest request) {

		

		String stringRefreshToken = refreshTokenService.extractRefreshToken(request);

		if (stringRefreshToken == null) {

			throw new AuthenticationCredentialsNotFoundException("missing refresh token");
		}

		RefreshToken rotationalRefreshToken = refreshTokenService
				.verifyAndGenerateRoataionalRefreshtoken(stringRefreshToken);

		String email= rotationalRefreshToken.getUser().getEmail();

		

		ResponseCookie accessCookie = jwtFilterUtility.generateToken(email,request);
		
		Long remainingSeconds = Duration.between(Instant.now(), rotationalRefreshToken.getExpiry()).getSeconds();
		
		
		ResponseCookie refreshCookie = 
				ResponseCookie.from("refreshToken", rotationalRefreshToken.getToken())
				.httpOnly(true)
				.secure(secureCookie)
				.path(request.getContextPath() + "/auth")
				.maxAge(remainingSeconds)
				.build();

		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, accessCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
				.build();
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response) {
     
		
		
		
		String refreshToken = refreshTokenService.extractRefreshToken(request);
		
		refreshTokenService.revokeRefreshToken(refreshToken);
		
		ResponseCookie accessTokenCookie = 
				ResponseCookie
				.from("accessToken", "")
				.httpOnly(true)
				.secure(secureCookie)
				.path(request.getContextPath() + "/")
				.maxAge(0) 
				.build();

		ResponseCookie refreshTokenCookie = 
				ResponseCookie
				.from("refreshToken", "")
				.httpOnly(true)
				.secure(secureCookie)
				.path(request.getContextPath() + "/auth")
				.maxAge(0) 
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

		return new ResponseEntity<>(new ApiResponseDto("200 OK", "Logged out sucessFully"),HttpStatus.OK);
	}

}
