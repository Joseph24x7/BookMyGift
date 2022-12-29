package com.bookmyticket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@ConfigurationProperties
@Getter
public class ApplicationProperty {
	
	@Value("${get_movie}")
	private Boolean getMovieAuthenticator;

}
