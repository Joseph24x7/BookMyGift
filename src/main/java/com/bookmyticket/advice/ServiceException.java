package com.bookmyticket.advice;

public class ServiceException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private String errorDescription;
	private ErrorEnums errorEnums;
	
	public ServiceException(ErrorEnums errorEnums) {
		super();
		this.setErrorEnums(errorEnums);
	}
	
	public ServiceException(ErrorEnums errorEnums,String errorDescription) {
		super();
		this.setErrorEnums(errorEnums);
		this.setErrorDescription(errorDescription);
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public ErrorEnums getErrorEnums() {
		return errorEnums;
	}
	public void setErrorEnums(ErrorEnums errorEnums) {
		this.errorEnums = errorEnums;
	}
	
}
