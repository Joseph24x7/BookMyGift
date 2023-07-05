package com.bookmygift.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorEnums errorEnums;

    public BadRequestException(ErrorEnums errorEnums) {
        this.errorEnums = errorEnums;
    }

    public BadRequestException(ErrorEnums errorEnums, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorEnums = errorEnums;
    }

}
