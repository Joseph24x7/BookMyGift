package com.bookmygift.service;

import org.springframework.stereotype.Service;

import com.bookmygift.entity.Order;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class EmailService {
	
    private final JavaMailSender javaMailSender;

    public void sendOrderConfirmationEmail(Order order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getEmailId());
        mailMessage.setSubject("Order Confirmation for Order ID: " + order.getOrderId());
        mailMessage.setText("Dear " + order.getUsername() + ",\n\n Your Order with Order ID: " + order.getOrderId() + " has been placed successfully.\n\nThank you for choosing us.\n\nRegards,\n My Company name");
        javaMailSender.send(mailMessage);
    }
}

