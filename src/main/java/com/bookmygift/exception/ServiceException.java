package com.bookmygift.exception;

public class ServiceException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private final String errorDescription;
	private final ErrorEnums errorEnums;
	
	public ServiceException(ErrorEnums errorEnums) {
		super();
		this.errorEnums = errorEnums;
		this.errorDescription = null;
	}
	
	public ServiceException(ErrorEnums errorEnums,String errorDescription) {
		super();
		this.errorEnums = errorEnums;
		this.errorDescription = errorDescription;
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}
	public ErrorEnums getErrorEnums() {
		return errorEnums;
	}
	
}
