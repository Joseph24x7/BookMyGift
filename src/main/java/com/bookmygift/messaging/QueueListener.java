package com.bookmygift.messaging;

import com.bookmygift.entity.OrderEntity;
import com.bookmygift.entity.UserEntity;
import com.bookmygift.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueListener {

    private final EmailService emailService;

    @RabbitListener(queues = "orderQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleOrderMessage(OrderEntity order) {
        emailService.sendOrderConfirmationEmail(order);
    }

    @RabbitListener(queues = "cancelQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleCancelMessage(OrderEntity order) {
        emailService.cancelOrderConfirmationEmail(order);
    }

    @RabbitListener(queues = "sendOtpQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSendOtpEmail(UserEntity user) {
        emailService.sendOtpEmail(user);
    }

    @RabbitListener(queues = "sendVerifySuccessQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSendVerifySuccessQueue(UserEntity user) {
        emailService.sendVerificationSuccessEmail(user);
    }
}