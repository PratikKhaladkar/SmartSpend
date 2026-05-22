package com.expense_tracker.GlobalExceptionHandling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.expense_tracker.Exceptions.EmailAlreadyExistsException;
import com.expense_tracker.Exceptions.ExpenseCategoryAlreadyExists;
import com.expense_tracker.Exceptions.ExpenseCatogoryNotFoundException;
import com.expense_tracker.Exceptions.ForbiddenException;
import com.expense_tracker.Exceptions.IncomeSourceAlredyExistsException;
import com.expense_tracker.Exceptions.IncomeSurceNotFoundException;
import com.expense_tracker.Exceptions.InsufficientPasswordLengthException;
import com.expense_tracker.Exceptions.InvalidRequestExecption;
import com.expense_tracker.Exceptions.InvalidTokenException;
import com.expense_tracker.Exceptions.ReusedTokenException;
import com.expense_tracker.Exceptions.TransactionNotFoundException;
import com.expense_tracker.Exceptions.UserNotFoundException;
import com.expense_tracker.dtos.ApiErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TransactionNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IncomeSurceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleIncomeSurceNotFoundException(IncomeSurceNotFoundException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ExpenseCatogoryNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleExpenseCatogoryNotFoundException(ExpenseCatogoryNotFoundException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(IncomeSourceAlredyExistsException.class)
	public ResponseEntity<ApiErrorResponse> handleIncomeSourceAlredyExistsException(
			IncomeSourceAlredyExistsException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ExpenseCategoryAlreadyExists.class)
	public ResponseEntity<ApiErrorResponse> handleExpenseCategoryAlreadyExists(ExpenseCategoryAlreadyExists e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiErrorResponse> handleForbiddenException(ForbiddenException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(InvalidRequestExecption.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidRequestExecption(InvalidRequestExecption e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidTokenException(InvalidTokenException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ReusedTokenException.class)
	public ResponseEntity<ApiErrorResponse> handleReusedTokenException(ReusedTokenException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {

		Map<String, String> errors = new HashMap<>();

		e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

	}

	@ExceptionHandler(InsufficientPasswordLengthException.class)
	public ResponseEntity<ApiErrorResponse> handleInsufficientPasswordLengthException(
			InsufficientPasswordLengthException e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

		String message = "Invalid request data";

		Throwable cause = e.getMostSpecificCause();

		if (cause instanceof InvalidFormatException ex) {

			Class<?> targetType = ex.getTargetType();

			if (targetType.isEnum()) {

				String values = Arrays.toString(targetType.getEnumConstants());

				message = "Invalid value '" + ex.getValue() + "'. Allowed values are: " + values;
			}
		}

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), message);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception e) {

		ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
