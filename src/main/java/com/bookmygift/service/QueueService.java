package com.bookmygift.service;

import com.bookmygift.entity.OrderEntity;
import com.bookmygift.utils.ApplicationConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows(JsonProcessingException.class)
    public void sendPlaceOrderSuccessNotification(OrderEntity orderEntity) {
        rabbitTemplate.convertAndSend(ApplicationConstants.DIRECT_EXCHANGE, ApplicationConstants.ORDER_ROUTING_KEY, objectMapper.writeValueAsString(orderEntity));
    }

    @SneakyThrows(JsonProcessingException.class)
    public void sendOrderCancelledNotification(OrderEntity orderEntity) {
        rabbitTemplate.convertAndSend(ApplicationConstants.DIRECT_EXCHANGE, ApplicationConstants.CANCEL_ROUTING_KEY, objectMapper.writeValueAsString(orderEntity));
    }

}
