package com.bookmygift.exception;

import java.util.function.Function;

@FunctionalInterface
public interface UncheckedFunction<T, R> {
	
    R apply(T t) throws Exception;
    
    public static <T, R> Function<T, R> unchecked(UncheckedFunction<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new ServiceException(ErrorEnums.GENERAL_EXCEPTION, e.getMessage());
            }
        };
    }

}
