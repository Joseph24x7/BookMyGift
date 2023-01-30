package com.bookmygift;

import org.springframework.stereotype.Service;

import com.bookmygift.entity.Order;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {
	
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender emailSender) {
        this.javaMailSender = emailSender;
    }
    
    public void sendOrderConfirmationEmail(Order order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getEmailId());
        mailMessage.setSubject("Order Confirmation for Order ID: " + order.getOrderId());
        mailMessage.setText("Dear " + order.getUsername() + ",\n\n Your Order with Order ID: " + order.getOrderId() + " has been placed successfully.\n\nThank you for choosing us.\n\nRegards,\nYour Company");
        javaMailSender.send(mailMessage);
    }
}

