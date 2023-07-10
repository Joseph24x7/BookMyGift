package com.bookmygift.service;

import com.bookmygift.entity.OrderEntity;
import com.bookmygift.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static com.bookmygift.utils.ApplicationConstants.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QueueServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private QueueService queueService;

    @Test
    public void test_sendPlaceOrderSuccessNotification() throws JsonProcessingException {

        OrderEntity order = new OrderEntity();
        queueService.sendPlaceOrderSuccessNotification(order);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, ORDER_ROUTING_KEY, objectMapper.writeValueAsString(order));
    }

    @Test
    public void test_sendOrderCancelledNotification() throws JsonProcessingException {

        OrderEntity order = new OrderEntity();
        queueService.sendOrderCancelledNotification(order);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, CANCEL_ROUTING_KEY, objectMapper.writeValueAsString(order));
    }

    @Test
    public void test_sendTwoFactorAuthentication() throws JsonProcessingException {

        UserEntity user = new UserEntity();
        queueService.sendTwoFactorAuthentication(user);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, SEND_OTP_ROUTING_KEY, objectMapper.writeValueAsString(user));
    }

    @Test
    public void test_sendVerificationSuccessNotification() throws JsonProcessingException {

        UserEntity user = new UserEntity();
        queueService.sendVerificationSuccessNotification(user);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, SEND_VERIFY_SUCCESS_ROUTING_KEY, objectMapper.writeValueAsString(user));
    }
}
