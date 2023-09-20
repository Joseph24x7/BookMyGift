package com.bookmygift.messaging;

import com.bookmygift.entity.OrderEntity;
import com.bookmygift.service.EmailService;
import com.bookmygift.utils.ApplicationConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueListener {

    private final EmailService emailService;

    @RabbitListener(queues = ApplicationConstants.ORDER_QUEUE, containerFactory = ApplicationConstants.RABBIT_LISTENER_CONTAINER_FACTORY)
    public void handleOrderMessage(OrderEntity orderEntity) {
        emailService.sendOrderConfirmationEmail(orderEntity);
    }

    @RabbitListener(queues = ApplicationConstants.CANCEL_QUEUE, containerFactory = ApplicationConstants.RABBIT_LISTENER_CONTAINER_FACTORY)
    public void handleCancelMessage(OrderEntity orderEntity) {
        emailService.cancelOrderConfirmationEmail(orderEntity);
    }

}
