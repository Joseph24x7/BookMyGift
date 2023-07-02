package com.bookmygift.config;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

}