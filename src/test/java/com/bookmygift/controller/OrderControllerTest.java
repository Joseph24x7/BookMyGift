package com.bookmygift.controller;

import com.bookmygift.entity.GiftTypeEnum;
import com.bookmygift.entity.OrderEntity;
import com.bookmygift.entity.OrderStatusEnum;
import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.response.OrderResponse;
import com.bookmygift.service.OrderService;
import com.bookmygift.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    String orderId = "order1";
    String username = "user1";
    @Mock
    private OrderService orderService;
    @Mock
    private TokenUtil tokenUtil;
    @Mock
    private HttpServletRequest servletRequest;
    @InjectMocks
    private OrderController orderController;
    private PlaceOrderRequest placeOrderRequest;
    private ShowOrderRequest showOrderRequest;
    private OrderResponse orderResponseWithEntities;
    private OrderResponse orderResponseWithEntity;

    @BeforeEach
    void setUp() {
        placeOrderRequest = PlaceOrderRequest.builder().giftType("FRAME").amountPaid(300.0D).username("user1").build();
        showOrderRequest = ShowOrderRequest.builder().giftType("FRAME").orderStatus("ORDER_RECEIVED").username("user1").build();
        OrderEntity orderEntity = OrderEntity.builder()
                .emailId("example@email.com").orderId("order1").giftType(GiftTypeEnum.FRAME).orderStatus(OrderStatusEnum.ORDER_RECEIVED)
                .amountPaid(300.0D).username("user1").build();
        orderResponseWithEntities = OrderResponse.builder().orderEntities(Collections.singletonList(orderEntity)).build();
        orderResponseWithEntity = OrderResponse.builder().orderEntity(orderEntity).build();
    }

    @Test
    void placeOrder() {
        when(tokenUtil.extractUsernameFromRequest(servletRequest)).thenReturn(username);
        when(orderService.placeOrder(placeOrderRequest)).thenReturn(orderResponseWithEntity);
        OrderResponse actualResponse = orderController.placeOrder(placeOrderRequest, servletRequest);
        verify(tokenUtil).extractUsernameFromRequest(servletRequest);
        verify(orderService).placeOrder(placeOrderRequest);
        assertEquals(orderResponseWithEntity, actualResponse);
    }

    @Test
    void showMyOrders() {
        when(tokenUtil.extractUsernameFromRequest(servletRequest)).thenReturn(username);
        when(orderService.showMyOrders(showOrderRequest)).thenReturn(orderResponseWithEntities);
        OrderResponse actualResponse = orderController.showMyOrders(showOrderRequest, servletRequest);
        verify(tokenUtil).extractUsernameFromRequest(servletRequest);
        verify(orderService).showMyOrders(showOrderRequest);
        assertEquals(orderResponseWithEntities, actualResponse);
    }

    @Test
    void cancelOrder() {
        when(tokenUtil.extractUsernameFromRequest(servletRequest)).thenReturn(username);
        when(orderService.cancelOrder(orderId, username)).thenReturn(orderResponseWithEntity);
        OrderResponse actualResponse = orderController.cancelOrder(orderId, servletRequest);
        verify(tokenUtil).extractUsernameFromRequest(servletRequest);
        verify(orderService).cancelOrder(orderId, username);
        assertEquals(orderResponseWithEntity, actualResponse);
    }
}

