package com.bookmygift.controller;

import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.response.ErrorResponse;
import com.bookmygift.utils.ErrorEnums;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleServiceException(UnAuthorizedException exception) {
        return populateException(exception.getErrorEnums());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        return populateException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        return populateException(HttpStatus.BAD_REQUEST, List.copyOf(errors).toString());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException validationEx) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return populateException(HttpStatus.BAD_REQUEST, List.copyOf(errors).toString());
    }

    public ErrorResponse populateException(ErrorEnums errorEnums) {

        return ErrorResponse.builder().errorType(errorEnums.getErrorCode()).errorDescription(errorEnums.getErrorDescription()).build();
    }

    public ErrorResponse populateException(HttpStatus httpStatus, String errorMessage) {

        return ErrorResponse.builder().errorType(httpStatus.name()).errorDescription(httpStatus.getReasonPhrase()).errorDetail(errorMessage).build();
    }

}
