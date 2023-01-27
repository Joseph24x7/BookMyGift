package com.bookmygift.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bookmygift.utils.CommonUtils;

import io.jsonwebtoken.MalformedJwtException;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final ObservationRegistry observationRegistry;

	@ExceptionHandler(ServiceException.class)
	public ProblemDetail handleServiceException(ServiceException serviceException, HttpServletRequest request) {

		return CommonUtils.populateException(serviceException.getErrorEnums().getHttpStatus(),
				serviceException.getErrorEnums().getErrorDescription(), serviceException.getErrorEnums().getErrorCode(),
				observationRegistry, request);

	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleServiceException(Exception exception, HttpServletRequest request) {

		return CommonUtils.populateException(ErrorEnums.GENERAL_EXCEPTION.getHttpStatus(), exception.getMessage(),
				ErrorEnums.GENERAL_EXCEPTION.getErrorCode(), observationRegistry, request);

	}

	@ExceptionHandler({BadCredentialsException.class,MalformedJwtException.class})
	public ProblemDetail handleServiceException(Exception exception, HttpServletRequest request,
			HttpServletResponse response) {

		return CommonUtils.populateException(ErrorEnums.UNAUTHORIZED.getHttpStatus(), exception.getMessage(),
				ErrorEnums.UNAUTHORIZED.getErrorCode(), observationRegistry, request);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ProblemDetail handleValidationExceptions(ConstraintViolationException ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return CommonUtils.populateException(HttpStatus.BAD_REQUEST, errors.toString(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), observationRegistry, request);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationExceptions(Exception ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();
		MethodArgumentNotValidException validationEx = (MethodArgumentNotValidException) ex;
		for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		return CommonUtils.populateException(HttpStatus.BAD_REQUEST, errors.toString(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), observationRegistry, request);
	}
	
}
