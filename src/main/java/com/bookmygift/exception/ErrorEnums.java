package com.bookmygift.exception;

public enum ErrorEnums {

    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid email or password."),
    INVALID_2FA_CODE("INVALID_2FA_CODE", "Invalid Two Factor Authentication code. Please enter a valid code and try again."),
    AUTHORIZATION_REQUIRED("AUTHORIZATION_REQUIRED", "Authorization token is required. Please provide a valid token to proceed."),
    USER_ALREADY_REGISTERED("USER_ALREADY_REGISTERED", "The selected username or email is not available."),
    INVALID_ORDER_ID("INVALID_ORDER_ID", "The Order Id is Invalid"),
    THEATRE_CODE_INVALID("THEATRE_CODE_INVALID", "The Given Theatre Code is Invalid"),
    TOKEN_REQUIRED("TOKEN_REQUIRED", "Please provide the token for authorization"),
    GENERAL_EXCEPTION("GENERAL_EXCEPTION");

    private final String errorCode;
    private final String errorDescription;

    ErrorEnums(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    ErrorEnums(String errorCode) {
        this.errorCode = errorCode;
        this.errorDescription = null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}
