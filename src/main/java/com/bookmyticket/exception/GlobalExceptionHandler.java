package com.bookmyticket.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;
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

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry).observe(() -> problemDetail);

	}

}
