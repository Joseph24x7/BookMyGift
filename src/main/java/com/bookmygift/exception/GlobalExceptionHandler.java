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

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(serviceException.getErrorEnums().getHttpStatus(),
				serviceException.getErrorEnums().getErrorDescription());
		problemDetail.setTitle(serviceException.getErrorEnums().getErrorCode());

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> problemDetail);

	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleServiceException(Exception exception, HttpServletRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ErrorEnums.GENERAL_EXCEPTION.getHttpStatus(),
				exception.getMessage());

		problemDetail.setTitle(ErrorEnums.GENERAL_EXCEPTION.getErrorCode());
		
		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> problemDetail);

	}

	@ExceptionHandler(BadCredentialsException.class)
	public ProblemDetail handleServiceException(Exception exception, HttpServletRequest request,
			HttpServletResponse response) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ErrorEnums.UNAUTHORIZED.getHttpStatus(),
				exception.getMessage());

		problemDetail.setTitle(ErrorEnums.UNAUTHORIZED.getErrorCode());

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> problemDetail);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ProblemDetail handleValidationExceptions(ConstraintViolationException ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
		problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> problemDetail);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationExceptions(Exception ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();
		MethodArgumentNotValidException validationEx = (MethodArgumentNotValidException) ex;
		for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
		problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> problemDetail);
	}

}
