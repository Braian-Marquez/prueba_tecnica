package com.tsg.posts.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.tsg.commons.exception.BadCredentialsException;
import com.tsg.commons.exception.ErrorDto;
import com.tsg.commons.exception.ErrorResponse;
import com.tsg.commons.exception.InsufficientPermissionsException;
import com.tsg.commons.exception.InvalidCredentialsException;
import com.tsg.commons.exception.NotFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalHandleException {
	private Logger logger = LoggerFactory.getLogger(GlobalHandleException.class);


	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorDto> handleInvalidCredentialsException(InvalidCredentialsException e) {
		ErrorDto response = new ErrorDto();
		ErrorResponse error = buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
		response.setError(error);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException e) {
		ErrorDto response = new ErrorDto();
		ErrorResponse error = buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
		response.setError(error);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	
	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<ErrorDto> handlBadCredentialsException(BadCredentialsException e) {
		ErrorDto response = new ErrorDto();
		ErrorResponse error = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
		response.setError(error);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InsufficientPermissionsException.class)
	public ResponseEntity<ErrorDto> handleInsufficientPermissionsException(InsufficientPermissionsException e) {
		ErrorDto response = new ErrorDto();
		ErrorResponse error = buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
		response.setError(error);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		List<String> errors = new ArrayList<>();

		ex.getConstraintViolations().forEach((violation) -> {
			errors.add(  violation.getMessage());
		});

		ErrorDto response = new ErrorDto();
		ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
		errorResponse.setDescription(errors);
		response.setError(errorResponse);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}



	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<>();

		ex.getBindingResult().getFieldErrors().forEach((error) -> {
			String errorMessage = error.getDefaultMessage();
			errors.add(errorMessage);
		});

		ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Error de validación");
		errorResponse.setDescription(errors);

		ErrorDto response = new ErrorDto();
		response.setError(errorResponse);

		return ResponseEntity.badRequest().body(response);
	}

	private ErrorResponse buildErrorResponse(HttpStatus httpStatus, String message) {
		ErrorResponse error = new ErrorResponse();
		error.setHttpStatusCode(httpStatus.value());
		error.getDescription().add(message);
		error.setTimestamp(LocalDateTime.now());
		return error;
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
	    String errorMessage = "Token has expired. Please reauthenticate or obtain a new token.";
	    logger.error("Token expired: {}", ex.getMessage());
	    Date expirationDate = ex.getClaims().getExpiration();
	    errorMessage += " Token expired at: " + expirationDate;
	    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.UNAUTHORIZED, errorMessage);
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}

}
