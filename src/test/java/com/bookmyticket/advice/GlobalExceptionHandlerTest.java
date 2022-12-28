package com.bookmyticket.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest
class GlobalExceptionHandlerTest {

	@Mock
	private HttpServletRequest request;

	@InjectMocks
	private GlobalExceptionHandler handler;

	@Test
	void testHandleServiceException() {
		
		ServiceException serviceException=new ServiceException(ErrorEnums.THEATRE_CODE_INVALID);
		
		when(request.getRequestURI()).thenReturn("/test");

		ProblemDetail result = handler.handleServiceException(serviceException, request);

		assertEquals(ErrorEnums.THEATRE_CODE_INVALID.getHttpStatus().value(), result.getStatus());
		assertEquals(ErrorEnums.THEATRE_CODE_INVALID.getErrorDescription(), result.getDetail());
		assertEquals(ErrorEnums.THEATRE_CODE_INVALID.getErrorCode(), result.getTitle());
	}
}