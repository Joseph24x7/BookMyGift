package com.bookmygift.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final ErrorEnums errorEnums;

    public ServiceException(ErrorEnums errorEnums) {
        this.errorEnums = errorEnums;
    }

    public ServiceException(ErrorEnums errorEnums, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorEnums = errorEnums;
    }

}
