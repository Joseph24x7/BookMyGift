package com.bookmygift.controller;

import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.response.ErrorResponse;
import com.bookmygift.utils.ErrorEnums;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleServiceException(BadRequestException exception) {
        return populateException(exception.getErrorEnums());
    }

    @ExceptionHandler({UnAuthorizedException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleServiceException(Exception exception) {
        if (exception instanceof UnAuthorizedException unAuthEx) {
            return populateException(unAuthEx.getErrorEnums());
        } else {
            return populateException(ErrorEnums.AUTHORIZATION_FAILED, exception.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        return populateException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(Exception ex) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof ConstraintViolationException constraintEx) {
            for (ConstraintViolation<?> violation : constraintEx.getConstraintViolations()) {
                errors.add(violation.getMessage());
            }
        } else if (ex instanceof MethodArgumentNotValidException validationEx) {
            for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
        }
        return populateException(HttpStatus.BAD_REQUEST.getReasonPhrase(), List.copyOf(errors).toString());
    }

    public ErrorResponse populateException(ErrorEnums errorEnums) {
        return ErrorResponse.builder().errorType(errorEnums.getErrorCode()).errorDescription(errorEnums.getErrorDescription()).build();
    }

    public ErrorResponse populateException(ErrorEnums errorEnums, String errorDetail) {
        return ErrorResponse.builder().errorType(errorEnums.getErrorCode()).errorDescription(errorEnums.getErrorDescription()).errorDetail(errorDetail).build();
    }

    public ErrorResponse populateException(String errorType, String errorMessage) {
        return ErrorResponse.builder().errorType(errorType).errorDetail(errorMessage).build();
    }

}
