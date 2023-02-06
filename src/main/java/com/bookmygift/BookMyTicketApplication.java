package com.bookmygift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookMyTicketApplication{

	public static void main(String[] args) {
		SpringApplication.run(BookMyTicketApplication.class, args);
	}

}