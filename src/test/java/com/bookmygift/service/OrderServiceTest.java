package com.bookmygift.service;

import com.bookmygift.entity.OrderEntity;
import com.bookmygift.entity.OrderStatusEnum;
import com.bookmygift.entity.UserEntity;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private OrderService orderService;

    private PlaceOrderRequest validOrderRequest;
    private UserEntity existingUser;

    @BeforeEach
    public void setup() {
        validOrderRequest = PlaceOrderRequest.builder().username("john123").giftType("FRAME").amountPaid(100.0).build();
        existingUser = UserEntity.builder().username("john123").email("john@example.com").build();
    }

    @Test
    public void testPlaceOrder_ValidRequest_ShouldReturnOrderResponse() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(queueService).sendPlaceOrderSuccessNotification(any(OrderEntity.class));

        OrderResponse result = orderService.placeOrder(validOrderRequest);

        verify(userRepository).findByUsername("john123");

        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(orderCaptor.capture());
        OrderEntity savedOrder = orderCaptor.getValue();

        assertNotNull(result);
        assertNotNull(result.getOrderEntity());
        assertEquals(savedOrder, result.getOrderEntity());

        verify(queueService).sendPlaceOrderSuccessNotification(savedOrder);
    }

    @Test
    public void testPlaceOrder_InvalidUsername_ShouldThrowUnAuthorizedException() {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UnAuthorizedException.class, () -> orderService.placeOrder(validOrderRequest));

        verify(userRepository).findByUsername("john123");
        verifyNoInteractions(orderRepository, queueService);
    }

    @Test
    public void testShowMyOrders_ShouldReturnOrderResponse() {
        List<OrderEntity> orderEntities = new ArrayList<>();
        when(orderRepository.findOrdersByCriteria(any(ShowOrderRequest.class))).thenReturn(orderEntities);

        OrderResponse result = orderService.showMyOrders(ShowOrderRequest.builder().build());

        verify(orderRepository).findOrdersByCriteria(any(ShowOrderRequest.class));

        assertNotNull(result);
        assertNotNull(result.getOrderEntities());
        assertEquals(orderEntities, result.getOrderEntities());
    }

    @Test
    public void testCancelOrder_ValidOrderIdAndUsername_ShouldReturnOrderResponse() {
        OrderEntity existingOrder = new OrderEntity();
        existingOrder.setOrderId("ABC_123");
        existingOrder.setUsername("john123");
        existingOrder.setOrderStatus(OrderStatusEnum.ORDER_RECEIVED);
        when(orderRepository.findByOrderIdAndUsername(anyString(), anyString())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(queueService).sendOrderCancelledNotification(any(OrderEntity.class));

        OrderResponse result = orderService.cancelOrder("ABC_123", "john123");

        verify(orderRepository).findByOrderIdAndUsername("ABC_123", "john123");

        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(orderCaptor.capture());
        OrderEntity savedOrder = orderCaptor.getValue();

        assertNotNull(result);
        assertNotNull(result.getOrderEntity());
        assertEquals(savedOrder, result.getOrderEntity());

        verify(queueService).sendOrderCancelledNotification(savedOrder);
    }

    @Test
    public void testCancelOrder_OrderAlreadyCancelled_ShouldThrowBadRequestException() {
        OrderEntity existingOrder = new OrderEntity();
        existingOrder.setOrderId("ABC_123");
        existingOrder.setUsername("john123");
        existingOrder.setOrderStatus(OrderStatusEnum.CANCELLED);
        when(orderRepository.findByOrderIdAndUsername(anyString(), anyString())).thenReturn(Optional.of(existingOrder));

        assertThrows(BadRequestException.class, () -> orderService.cancelOrder("ABC_123", "john123"));

        verify(orderRepository).findByOrderIdAndUsername("ABC_123", "john123");
        verifyNoInteractions(queueService);
    }

}
