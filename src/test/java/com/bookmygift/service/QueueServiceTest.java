package com.bookmygift.service;

import com.bookmygift.entity.Order;
import com.bookmygift.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static com.bookmygift.utils.ApplicationConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
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

        Order order = new Order();
        queueService.sendPlaceOrderSuccessNotification(order);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, ORDER_ROUTING_KEY, objectMapper.writeValueAsString(order));
    }

    @Test
    public void test_sendOrderCancelledNotification() throws JsonProcessingException {

        Order order = new Order();
        queueService.sendOrderCancelledNotification(order);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, CANCEL_ROUTING_KEY, objectMapper.writeValueAsString(order));
    }

    @Test
    public void test_sendTwoFactorAuthentication() throws JsonProcessingException {

        User user = new User();
        queueService.sendTwoFactorAuthentication(user);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, SEND_OTP_ROUTING_KEY, objectMapper.writeValueAsString(user));
    }

    @Test
    public void test_sendVerificationSuccessNotification() throws JsonProcessingException {

        User user = new User();
        queueService.sendVerificationSuccessNotification(user);

        verify(rabbitTemplate).convertAndSend(DIRECT_EXCHANGE, SEND_VERIFY_SUCCESS_ROUTING_KEY, objectMapper.writeValueAsString(user));
    }

    @Test
    public void testSendPlaceOrderSuccessNotification_JsonProcessingException() throws JsonProcessingException {

        Order order = new Order();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(order);

        assertThrows(JsonProcessingException.class, () -> queueService.sendPlaceOrderSuccessNotification(order));
    }

    @Test
    public void testSendOrderCancelledNotification_JsonProcessingException() throws JsonProcessingException {

        Order order = new Order();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(order);

        assertThrows(JsonProcessingException.class, () -> queueService.sendOrderCancelledNotification(order));
    }

    @Test
    public void testSendTwoFactorAuthentication_JsonProcessingException() throws JsonProcessingException {

        User user = new User();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(user);

        assertThrows(JsonProcessingException.class, () -> queueService.sendTwoFactorAuthentication(user));
    }

    @Test
    public void testSendVerificationSuccessNotification_JsonProcessingException() throws JsonProcessingException {

        User user = new User();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(user);

        assertThrows(JsonProcessingException.class, () -> queueService.sendVerificationSuccessNotification(user));
    }
}
