package com.bookmygift.controller;

import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.response.ErrorResponse;
import com.bookmygift.utils.ErrorEnums;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionControllerTest {

    private final GlobalExceptionController globalExceptionController = new GlobalExceptionController();
    private final BadRequestException badRequestException = mock(BadRequestException.class);
    private final UnAuthorizedException unAuthorizedException = mock(UnAuthorizedException.class);
    private final ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
    private final MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
    private final BadCredentialsException badCredentialsException = mock(BadCredentialsException.class);
    private final ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
    private final FieldError fieldError = mock(FieldError.class);
    private final BindingResult bindingResult = mock(BindingResult.class);
    ErrorEnums invalidOrderIdEnum = ErrorEnums.INVALID_ORDER_ID;
    ErrorEnums unauthorizedEnum = ErrorEnums.AUTHORIZATION_FAILED;

    ErrorResponse badRequestResponse = ErrorResponse.builder().errorDetail("[BAD_REQUEST]").errorType(HttpStatus.BAD_REQUEST.getReasonPhrase()).build();

    @Test
    void handleBadRequestException() {
        when(badRequestException.getErrorEnums()).thenReturn(invalidOrderIdEnum);
        ErrorResponse response = globalExceptionController.handleBadRequestException(badRequestException);
        assertEquals(invalidOrderIdEnum.getErrorCode(), response.getErrorType());
        assertEquals(invalidOrderIdEnum.getErrorDescription(), response.getErrorDescription());
    }

    @Test
    void handleAuthorizationException_UnAuthorizedException() {
        when(unAuthorizedException.getErrorEnums()).thenReturn(unauthorizedEnum);
        ErrorResponse response = globalExceptionController.handleAuthorizationException(unAuthorizedException);
        assertEquals(unauthorizedEnum.getErrorCode(), response.getErrorType());
        assertEquals(unauthorizedEnum.getErrorDescription(), response.getErrorDescription());
    }

    @Test
    void handleAuthorizationException_BadCredentialsException() {
        ErrorResponse response = globalExceptionController.handleAuthorizationException(badCredentialsException);
        assertEquals(unauthorizedEnum.getErrorCode(), response.getErrorType());
        assertEquals(unauthorizedEnum.getErrorDescription(), response.getErrorDescription());
    }

    @Test
    void handleException() {
        String errorMessage = "Some error occurred";
        ErrorResponse response = globalExceptionController.handleException(new Exception(errorMessage));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), response.getErrorType());
        assertEquals(errorMessage, response.getErrorDetail());
    }

    @Test
    void handleConstraintViolationException_ConstraintViolationException() {
        when(constraintViolation.getMessage()).thenReturn(HttpStatus.BAD_REQUEST.name());
        when(constraintViolationException.getConstraintViolations()).thenReturn(Set.of(constraintViolation));
        ErrorResponse response = globalExceptionController.handleConstraintViolationException(constraintViolationException);
        assertEquals(badRequestResponse, response);
    }

    @Test
    void handleConstraintViolationException_MethodArgumentNotValidException() {
        when(fieldError.getDefaultMessage()).thenReturn(HttpStatus.BAD_REQUEST.name());
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        ErrorResponse response = globalExceptionController.handleConstraintViolationException(methodArgumentNotValidException);
        assertEquals(badRequestResponse, response);
    }
}

