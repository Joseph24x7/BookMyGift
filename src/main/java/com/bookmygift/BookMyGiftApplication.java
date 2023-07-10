package com.bookmygift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookMyGiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyGiftApplication.class, args);
    }

}