package com.bookmygift.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

	public static HttpServletRequest getRequest() {
		
		var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes!=null ? requestAttributes.getRequest() : null;
		
	}

}
