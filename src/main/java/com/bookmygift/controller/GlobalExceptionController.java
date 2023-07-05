package com.bookmygift.controller;

import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.UnAuthorizedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
    public ProblemDetail handleServiceException(BadRequestException exception) {
        return populateException(HttpStatus.BAD_REQUEST, exception.getErrorEnums());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleServiceException(UnAuthorizedException exception) {
        return populateException(HttpStatus.BAD_REQUEST, exception.getErrorEnums());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleException(Exception exception) {
        return populateException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name(), exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        return populateException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name(), List.copyOf(errors).toString());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException validationEx) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return populateException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name(), List.copyOf(errors).toString());
    }

    public ProblemDetail populateException(HttpStatus httpStatus, ErrorEnums errorEnums) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorEnums.getErrorDescription());
        problemDetail.setTitle(errorEnums.getErrorCode());
        return problemDetail;
    }

    public ProblemDetail populateException(HttpStatus httpStatus, String errorCode, String errorMessage) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorMessage);
        problemDetail.setTitle(errorCode);
        return problemDetail;
    }

}
