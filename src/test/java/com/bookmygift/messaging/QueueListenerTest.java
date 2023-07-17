package com.bookmygift.messaging;

import com.bookmygift.entity.Order;
import com.bookmygift.entity.User;
import com.bookmygift.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QueueListenerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private QueueListener queueListener;

    Order order = Order.builder().build();
    User user = User.builder().build();

    @Test
    public void shouldSendOrderConfirmationEmail() {
        queueListener.handleOrderMessage(order);
        verify(emailService).sendOrderConfirmationEmail(order);
    }

    @Test
    public void shouldCancelOrderConfirmationEmail() {
        queueListener.handleCancelMessage(order);
        verify(emailService).cancelOrderConfirmationEmail(order);
    }

    @Test
    public void shouldSendOtpEmail() {
        queueListener.handleSendOtpEmail(user);
        verify(emailService).sendOtpEmail(user);
    }

    @Test
    public void shouldSendVerificationSuccessEmail() {
        queueListener.handleSendVerifySuccessQueue(user);
        verify(emailService).sendVerificationSuccessEmail(user);
    }
}
