package com.bookmygift.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ErrorEnums.GENERAL_EXCEPTION.getHttpStatus(), exception.getMessage());

		problemDetail.setTitle(ErrorEnums.GENERAL_EXCEPTION.getErrorCode());

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> problemDetail);

	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ProblemDetail handleServiceException(Exception exception, HttpServletRequest request,
			HttpServletResponse response) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ErrorEnums.UNAUTHORIZED.getHttpStatus(), exception.getMessage());

		problemDetail.setTitle(ErrorEnums.UNAUTHORIZED.getErrorCode());

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> problemDetail);

	}

}
