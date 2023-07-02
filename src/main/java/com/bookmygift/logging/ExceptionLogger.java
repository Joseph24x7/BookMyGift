package com.bookmygift.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLogger {

    @AfterThrowing(pointcut = "execution(public * com.bookmygift..*(..)))", throwing = "ex")
    public void logException(Exception ex) {
        log.error("An exception was thrown: ", ex);
    }

}
