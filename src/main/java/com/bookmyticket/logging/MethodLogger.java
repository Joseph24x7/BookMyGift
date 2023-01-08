package com.bookmyticket.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLogger {

	@Around("publicMethod()")
	public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {
	  final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
	  logger.debug("Execution of {} Started", joinPoint.getSignature());
	  Object proceed = joinPoint.proceed();
	  logger.debug("Execution of {} completed", joinPoint.getSignature());
	  return proceed;
	}
	
	@Pointcut("execution(public * com.bookmyticket..*(..))")
	public void publicMethod() {}

}