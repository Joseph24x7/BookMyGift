package com.bookmygift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorEnums {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    EMAIL_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_REGISTERED", "The email address is already registered. Please use a different email address."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid email or password. Please check your credentials and try again."),
    INVALID_2FA_CODE(HttpStatus.UNAUTHORIZED, "INVALID_2FA_CODE", "Invalid Two Factor Authentication code. Please enter a valid code and try again."),
    AUTHORIZATION_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTHORIZATION_REQUIRED", "Authorization token is required. Please provide a valid token to proceed."),
    USER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "USER_ALREADY_REGISTERED", "The selected username or email is not available."),
    INVALID_ORDER_ID(HttpStatus.BAD_REQUEST, "INVALID_ORDER_ID", "The Order Id is Invalid"),
    THEATRE_CODE_INVALID(HttpStatus.BAD_REQUEST, "THEATRE_CODE_INVALID", "The Given Theatre Code is Invalid"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ACCESS", "Invalid Username/Password"),
    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "TOKEN_REQUIRED", "Please provide the token for authorization"),
    GENERAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_EXCEPTION", "General Exception occurred");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorDescription;

    ErrorEnums(HttpStatus httpStatus, String errorCode, String errorDescription) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    ErrorEnums(HttpStatus httpStatus, String errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorDescription = null;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}
