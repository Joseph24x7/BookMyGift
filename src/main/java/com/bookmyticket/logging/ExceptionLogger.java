package com.bookmyticket.logging;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ExceptionLogger {
	
	@AfterThrowing(pointcut = "execution(* com.bookmyticket..*(..)) && within(@org.springframework.web.bind.annotation.RestController *)", throwing = "ex")
    public void logException(Exception ex) {
        log.error("An exception was thrown: {}", ex);
    }
	
}
