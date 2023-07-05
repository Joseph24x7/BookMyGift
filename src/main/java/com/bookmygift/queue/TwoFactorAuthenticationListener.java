package com.bookmygift.queue;

import com.bookmygift.entity.User;
import com.bookmygift.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthenticationListener {

    private final EmailService emailService;

    @RabbitListener(queues = "sendOtpQueue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSendOtpEmail(User user) {
        emailService.sendOtpEmail(user);
    }

}
