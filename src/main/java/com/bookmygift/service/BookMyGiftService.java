package com.bookmygift.service;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.entity.User;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.utils.ErrorEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookMyGiftService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final QueueService queueService;

    public Order placeOrder(PlaceOrderRequest orderRequest) {

        String username = orderRequest.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UnAuthorizedException(ErrorEnums.INVALID_CREDENTIALS));

        Order order = Order.builder().orderId(username.substring(0, 3).toUpperCase() + "_" + UUID.randomUUID()).username(username).emailId(user.getEmail()).giftType(GiftType.valueOf(orderRequest.getGiftType())).amountPaid(orderRequest.getAmountPaid()).orderStatus(OrderStatus.ORDER_RECEIVED).build();

        orderRepository.save(order);

        queueService.sendOrderRoutingKey(order);

        return order;

    }

    public List<Order> showMyOrders(ShowOrderRequest orderRequest) {
        return orderRepository.findOrdersByCriteria(orderRequest);
    }

    public Order cancelOrder(String orderId, String username) {

        Order order = orderRepository.findByOrderIdAndUsername(orderId, username).orElseThrow(() -> new BadRequestException(ErrorEnums.INVALID_ORDER_ID));

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        queueService.sendCancelRoutingKey(order);

        return order;

    }

}
