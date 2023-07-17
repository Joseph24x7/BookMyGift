package com.bookmygift.service;

import com.bookmygift.entity.Order;
import com.bookmygift.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    private Order order;
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        order = Order.builder().emailId("test@example.com").orderId(String.valueOf(123)).username("John Doe").build();
        user = UserEntity.builder().email("test@example.com").fullName("John Doe").twoFaCode("123456").build();
    }

    @Test
    public void testSendOrderConfirmationEmail_OrderConfirmationSent() {

        emailService.sendOrderConfirmationEmail(order);

        verify(javaMailSender).send(createOrderConfirmationEmailMessage(order));
    }

    @Test
    public void testCancelOrderConfirmationEmail_OrderCancellationSent() {

        emailService.cancelOrderConfirmationEmail(order);

        verify(javaMailSender).send(createCancelOrderConfirmationEmailMessage(order));
    }

    @Test
    public void testSendOtpEmail_OTPSent() {

        emailService.sendOtpEmail(user);

        verify(javaMailSender).send(createOtpEmailMessage(user));
    }

    @Test
    public void testSendVerificationSuccessEmail_VerificationSuccessSent() {

        emailService.sendVerificationSuccessEmail(user);

        verify(javaMailSender).send(createVerificationSuccessEmailMessage(user));
    }

    private SimpleMailMessage createOrderConfirmationEmailMessage(Order order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getEmailId());
        mailMessage.setSubject("Order Confirmation for Order ID: " + order.getOrderId());
        mailMessage.setText("Dear " + order.getUsername() + ",\n\n We are thrilled to confirm your order with Order ID: " + order.getOrderId() + " has been placed successfully!\n\n"
                + "Your order will be delivered to you soon, and we can't wait for you to enjoy your purchase.\n\n"
                + "Thank you for choosing us.\n\n"
                + "Warm Regards,\n"
                + "The My Company Name Team");
        return mailMessage;
    }

    private SimpleMailMessage createCancelOrderConfirmationEmailMessage(Order order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getEmailId());
        mailMessage.setSubject("Order Cancellation for Order ID: " + order.getOrderId());
        mailMessage.setText("Dear " + order.getUsername() + ",\n\n We are sorry to hear that you had to cancel your order with Order ID: " + order.getOrderId() + ".\n\n"
                + "If there was anything we could have done to make your experience better, please let us know. Your feedback is important to us and helps us improve.\n\n"
                + "Thank you for choosing My Company Name, and we hope to have the opportunity to serve you again in the future.\n\n"
                + "Warm Regards,\n"
                + "The My Company Name Team");
        return mailMessage;
    }

    private SimpleMailMessage createOtpEmailMessage(UserEntity user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("OTP for Login");
        mailMessage.setFrom("Book My Gift Team <" + user.getEmail() + ">");
        mailMessage.setText("Dear " + user.getFullName() + ",\n\n Your OTP for login is: " + user.getTwoFaCode()
                + ".\n\n" + "Please enter this OTP to proceed with your login.\n\n" + "Thank you for choosing us.\n\n"
                + "Warm Regards,\n" + "Book My Gift Team");
        return mailMessage;
    }

    private SimpleMailMessage createVerificationSuccessEmailMessage(UserEntity user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Verification Success");
        mailMessage.setFrom("Book My Gift Team <" + user.getEmail() + ">");
        mailMessage.setText(
                "Dear " + user.getFullName() + ",\n\n Your verification is successful. Please enjoy using BookMyGift."
                        + "\n\n" + "Thank you for choosing us.\n\n" + "Warm Regards,\n" + "Book My Gift Team");
        return mailMessage;
    }
}
