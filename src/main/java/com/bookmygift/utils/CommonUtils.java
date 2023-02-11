package com.bookmygift.utils;

import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonUtils {
	
	private final Validator validator;

	public Set<ConstraintViolation<?>> validate(Object object) {
		Set<ConstraintViolation<Object>> violations = validator.validate(object);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return null;
	}

}
