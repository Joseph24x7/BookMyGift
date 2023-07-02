package com.bookmygift.exception;

public class ServiceException extends RuntimeException {

    private final String errorDescription;
    private final ErrorEnums errorEnums;

    public ServiceException(ErrorEnums errorEnums) {
        super();
        this.errorEnums = errorEnums;
        this.errorDescription = null;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public ErrorEnums getErrorEnums() {
        return errorEnums;
    }

}
