package com.bookmygift.service;

import org.springframework.stereotype.Service;

import com.bookmygift.entity.Order;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class Java12 {

    public static void main(String[] args) {

        // Switch Expressions: This feature extends the switch statement to be used as
        // either a statement or an expression, and includes the ability to use the
        // yield keyword to return values.

        int num = 4;
        String result = switch (num) {
            case 1 -> "One";
            case 2 -> "Two";
            case 3 -> "Three";
            default -> "Other";
        };
        System.out.println(result);


        // The stream() method on the Optional class was introduced in Java 12 as a way
        // to convert an Optional into a Stream. By converting an Optional into a
        // Stream, it becomes possible to perform operations on it, such as mapping or
        // filtering, that are usually performed on collections. This allows for a more
        // concise and readable code when working with Optional values.

        Optional<String> opt = Optional.of("Hello");
        opt.stream().forEach(System.out::println);

    }

}
