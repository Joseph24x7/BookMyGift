package com.bookmygift.service;

import com.bookmygift.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendOrderConfirmationEmail(Order order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getEmailId());
        mailMessage.setSubject("Order Confirmation for Order ID: " + order.getOrderId());
        mailMessage.setText("Dear " + order.getUsername() + ",\n\n We are thrilled to confirm your order with Order ID: " + order.getOrderId() + " has been placed successfully!\n\n"
                + "Your order will be delivered to you soon, and we can't wait for you to enjoy your purchase.\n\n"
                + "Thank you for choosing us.\n\n"
                + "Warm Regards,\n"
                + "The My Company Name Team");
        javaMailSender.send(mailMessage);
    }

    public void cancelOrderConfirmationEmail(Order order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getEmailId());
        mailMessage.setSubject("Order Cancellation for Order ID: " + order.getOrderId());
        mailMessage.setText("Dear " + order.getUsername() + ",\n\n We are sorry to hear that you had to cancel your order with Order ID: " + order.getOrderId() + ".\n\n"
                + "If there was anything we could have done to make your experience better, please let us know. Your feedback is important to us and helps us improve.\n\n"
                + "Thank you for choosing My Company Name, and we hope to have the opportunity to serve you again in the future.\n\n"
                + "Warm Regards,\n"
                + "The My Company Name Team");
        javaMailSender.send(mailMessage);
    }

}

