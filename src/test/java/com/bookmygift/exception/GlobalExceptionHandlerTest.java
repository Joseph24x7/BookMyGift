package com.bookmygift.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
class GlobalExceptionHandlerTest {

	@Mock
	private HttpServletRequest request;

	@InjectMocks
	private GlobalExceptionHandler handler;

	@Test
	void testHandleServiceException() {

		ServiceException serviceException = new ServiceException(ErrorEnums.THEATRE_CODE_INVALID);

		when(request.getRequestURI()).thenReturn("/test");

		ProblemDetail result = handler.handleServiceException(serviceException, request);

		assertEquals(ErrorEnums.THEATRE_CODE_INVALID.getHttpStatus().value(), result.getStatus());
		assertEquals(ErrorEnums.THEATRE_CODE_INVALID.getErrorDescription(), result.getDetail());
		assertEquals(ErrorEnums.THEATRE_CODE_INVALID.getErrorCode(), result.getTitle());
	}
	
	@Test
	void testHandleException() {

		when(request.getRequestURI()).thenReturn("/test");

		ProblemDetail result = handler.handleException(new Exception("Error Message"), request);

		assertEquals(ErrorEnums.GENERAL_EXCEPTION.getHttpStatus().value(), result.getStatus());
		assertEquals("Error Message", result.getDetail());
		assertEquals(ErrorEnums.GENERAL_EXCEPTION.getErrorCode(), result.getTitle());
	}
	
	@Test
	void testHandleCredentialsException() {

		when(request.getRequestURI()).thenReturn("/test");

		ProblemDetail result = handler.handleCredentialsException(new Exception("Error Message"), request);

		assertEquals(ErrorEnums.UNAUTHORIZED.getHttpStatus().value(), result.getStatus());
		assertEquals("Error Message", result.getDetail());
		assertEquals(ErrorEnums.UNAUTHORIZED.getErrorCode(), result.getTitle());
	}
	

	@Test
	void testHandleConstraintViolationException() {
		
		Set<ConstraintViolation<Object>> violations = new HashSet<>();
		ConstraintViolationException ex = new ConstraintViolationException(violations);

		when(request.getRequestURI()).thenReturn("/test");

		ProblemDetail result = handler.handleConstraintViolationException(ex, request);

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), result.getTitle());
	}
	
	@Test
	void testMethodArgumentNotValidException() {
		
		when(request.getRequestURI()).thenReturn("/test");
		
		MethodArgumentNotValidException validationEx = new MethodArgumentNotValidException(null, new BeanPropertyBindingResult("objectName", "String"));

		ProblemDetail result = handler.handleMethodArgumentNotValidException(validationEx, request);

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), result.getTitle());
	}
}
