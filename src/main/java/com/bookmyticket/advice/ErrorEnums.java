package com.bookmyticket.advice;

import org.springframework.http.HttpStatus;

public enum ErrorEnums {
	
	THEATRE_CODE_INVALID(HttpStatus.BAD_REQUEST,"THEATRE_CODE_INVALID","The Given Theatre Code is Invalid"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"UNAUTHORIZED_ACCESS","Invalid Username/Password"),;
	
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
