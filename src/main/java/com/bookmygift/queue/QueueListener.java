package com.bookmygift.queue;

import com.bookmygift.entity.Order;
import com.bookmygift.entity.User;
import com.bookmygift.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueListener {

    private final EmailService emailService;

    @RabbitListener(queues = "orderQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleOrderMessage(Order order) {
        emailService.sendOrderConfirmationEmail(order);
    }

    @RabbitListener(queues = "cancelQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleCancelMessage(Order order) {
        emailService.cancelOrderConfirmationEmail(order);
    }

    @RabbitListener(queues = "sendOtpQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSendOtpEmail(User user) {
        emailService.sendOtpEmail(user);
    }

    @RabbitListener(queues = "sendVerifySuccessQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSendVerifySuccessQueue(User user) {
        emailService.sendVerificationSuccessEmail(user);
    }
}
