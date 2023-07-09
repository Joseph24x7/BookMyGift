package com.bookmygift.controller;

import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.response.ErrorResponse;
import com.bookmygift.utils.ErrorEnums;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException exception) {
        log.error("BadRequestException occurred: ", exception);
        return populateException(exception.getErrorEnums());
    }

    @ExceptionHandler({UnAuthorizedException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthorizationException(Exception exception) {
        log.error("AuthorizationException occurred: ", exception);
        if (exception instanceof UnAuthorizedException unAuthEx) {
            return populateException(unAuthEx.getErrorEnums());
        } else {
            return populateException(exception.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        log.error("Exception occurred: ", exception);
        return populateException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(Exception exception) {
        log.error("ConstraintViolationException occurred: ", exception);
        List<String> errors = new ArrayList<>();
        if (exception instanceof ConstraintViolationException constraintEx) {
            for (ConstraintViolation<?> violation : constraintEx.getConstraintViolations()) {
                errors.add(violation.getMessage());
            }
        } else if (exception instanceof MethodArgumentNotValidException validationEx) {
            for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
        }
        return populateException(HttpStatus.BAD_REQUEST.getReasonPhrase(), List.copyOf(errors).toString());
    }

    private ErrorResponse populateException(ErrorEnums errorEnums) {
        return ErrorResponse.builder().errorType(errorEnums.getErrorCode()).errorDescription(errorEnums.getErrorDescription()).build();
    }

    private ErrorResponse populateException(String errorDetail) {
        return ErrorResponse.builder().errorType(ErrorEnums.AUTHORIZATION_FAILED.getErrorCode()).errorDescription(ErrorEnums.AUTHORIZATION_FAILED.getErrorDescription()).errorDetail(errorDetail).build();
    }

    private ErrorResponse populateException(String errorType, String errorMessage) {
        return ErrorResponse.builder().errorType(errorType).errorDetail(errorMessage).build();
    }

}
