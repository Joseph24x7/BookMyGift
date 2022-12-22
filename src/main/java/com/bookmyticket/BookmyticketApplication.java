package com.bookmyticket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BookmyticketApplication implements WebMvcConfigurer {

	public static List<String> recommendedMovies = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(BookmyticketApplication.class, args);
	}

	@PostConstruct
	public void init() {

		recommendedMovies.add("Avatar");
		recommendedMovies.add("Connect");
		recommendedMovies.add("Thunivu");
		recommendedMovies.add("Varisu");

	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		jsonConverter.setObjectMapper(objectMapper);
		converters.add(jsonConverter);
		converters.add(new StringHttpMessageConverter());
	}

}
