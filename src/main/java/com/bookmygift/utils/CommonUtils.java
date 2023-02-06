package com.bookmygift.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtils {

	private CommonUtils() {

	}

	public static HttpServletRequest getRequest() {
		
		var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		
		return requestAttributes!=null ? requestAttributes.getRequest() : null;
		
	}
	
	

}
