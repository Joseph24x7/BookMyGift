package com.bookmygift.utils;

public enum ErrorEnums {

    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid email or password."),
    INVALID_2FA_CODE("INVALID_2FA_CODE", "Invalid Two Factor Authentication code. Please enter a valid code and try again."),
    AUTHORIZATION_REQUIRED("AUTHORIZATION_REQUIRED", "Authorization token is required. Please provide a valid token to proceed."),
    AUTHORIZATION_FAILED("AUTHORIZATION_FAILED", "Username or Password is incorrect. Please contact the administrator."),
    USER_ALREADY_REGISTERED("USER_ALREADY_REGISTERED", "The selected username or email is not available."),
    TWO_FA_ALREADY_VERIFIED("TWO_FA_ALREADY_VERIFIED", "Two Factor Verified done already. Please try again after sometime."),
    INVALID_ORDER_ID("INVALID_ORDER_ID", "The Order Id is Invalid"),
    TOKEN_REQUIRED("TOKEN_REQUIRED", "Please provide the token for authorization");

    private final String errorCode;
    private final String errorDescription;

    ErrorEnums(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}
