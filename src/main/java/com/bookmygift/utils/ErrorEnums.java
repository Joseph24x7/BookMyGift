package com.bookmygift.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnums {

    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid email or password."),
    AUTHORIZATION_FAILED("AUTHORIZATION_FAILED", "Username or Password is incorrect. Please contact the administrator."),
    INVALID_ORDER_ID("INVALID_ORDER_ID", "The OrderId is Invalid"),
    ORDER_ID_ALREADY_CANCELLED("ORDER_ID_ALREADY_CANCELLED", "The OrderId is cancelled already");

    private final String errorCode;
    private final String errorDescription;

}
