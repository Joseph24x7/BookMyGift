package com.bookmygift.service;

import com.bookmygift.entity.GiftTypeEnum;
import com.bookmygift.entity.OrderEntity;
import com.bookmygift.entity.OrderStatusEnum;
import com.bookmygift.entity.UserEntity;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserInfoRepository;
import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.response.OrderResponse;
import com.bookmygift.utils.ErrorEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final QueueService queueService;
    private final UserInfoRepository userInfoRepository;

    public OrderResponse placeOrder(PlaceOrderRequest orderRequest) {

        String email = orderRequest.getEmail();

        UserEntity user = userInfoRepository.findByEmail(email).orElseThrow(() -> new UnAuthorizedException(ErrorEnums.INVALID_CREDENTIALS));

        OrderEntity orderEntity = OrderEntity.builder().orderId(email.substring(0, 3).toUpperCase() + "_" + UUID.randomUUID()).emailId(user.getEmail()).
                giftType(GiftTypeEnum.valueOf(orderRequest.getGiftType())).amountPaid(orderRequest.getAmountPaid()).orderStatus(OrderStatusEnum.ORDER_RECEIVED).build();

        orderRepository.save(orderEntity);

        queueService.sendPlaceOrderSuccessNotification(orderEntity);

        return OrderResponse.builder().orderEntity(orderEntity).build();

    }

    public OrderResponse showMyOrders(ShowOrderRequest orderRequest) {

        List<OrderEntity> orderEntities = orderRepository.findOrdersByCriteria(orderRequest);

        return OrderResponse.builder().orderEntities(orderEntities).build();

    }

    public OrderResponse cancelOrder(String orderId, String email) {

        OrderEntity orderEntity = orderRepository.findByOrderIdAndEmailId(orderId, email).orElseThrow(() -> new BadRequestException(ErrorEnums.INVALID_ORDER_ID));

        if (OrderStatusEnum.CANCELLED.equals(orderEntity.getOrderStatus())) {

            throw new BadRequestException(ErrorEnums.ORDER_ID_ALREADY_CANCELLED);

        }

        orderEntity.setOrderStatus(OrderStatusEnum.CANCELLED);

        orderRepository.save(orderEntity);

        queueService.sendOrderCancelledNotification(orderEntity);

        return OrderResponse.builder().orderEntity(orderEntity).build();

    }

}
