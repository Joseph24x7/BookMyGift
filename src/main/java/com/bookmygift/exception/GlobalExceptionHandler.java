package com.bookmygift.exception;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObservationRegistry observationRegistry;

    @ExceptionHandler(ServiceException.class)
    public ProblemDetail handleServiceException(ServiceException serviceException, HttpServletRequest request) {

        return populateException(serviceException.getErrorEnums().getHttpStatus(),
                serviceException.getErrorDescription() != null ? serviceException.getErrorDescription()
                        : serviceException.getErrorEnums().getErrorDescription(),
                serviceException.getErrorEnums().getErrorCode(), request);

    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception, HttpServletRequest request) {

        return populateException(ErrorEnums.GENERAL_EXCEPTION.getHttpStatus(), exception.getMessage(),
                ErrorEnums.GENERAL_EXCEPTION.getErrorCode(), request);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex,
                                                            HttpServletRequest request) {

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        return populateException(HttpStatus.BAD_REQUEST, List.copyOf(errors).toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException validationEx,
                                                               HttpServletRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return populateException(HttpStatus.BAD_REQUEST, List.copyOf(errors).toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request);
    }

    public ProblemDetail populateException(HttpStatus httpStatus, String errorDescription, String errorCode,
                                           HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorDescription);
        problemDetail.setTitle(errorCode);

        return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
                .observe(() -> problemDetail);

    }

}
