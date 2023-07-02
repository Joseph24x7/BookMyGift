package com.bookmygift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorEnums {

    USER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "USER_ALREADY_REGISTERED", "The selected username or email is not available."),
    INVALID_ORDER_ID(HttpStatus.BAD_REQUEST, "INVALID_ORDER_ID", "The Order Id is Invalid"),
    THEATRE_CODE_INVALID(HttpStatus.BAD_REQUEST, "THEATRE_CODE_INVALID", "The Given Theatre Code is Invalid"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ACCESS", "Invalid Username/Password"),
    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "TOKEN_REQUIRED", "Please provide the token for authorization"),
    GENERAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_EXCEPTION", "General Exception occured");

    HttpStatus httpStatus;
    String errorCode;
    String errorDescription;

    private ErrorEnums(HttpStatus httpStatus, String errorCode, String errorDescription) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
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
