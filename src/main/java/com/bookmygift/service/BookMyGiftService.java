package com.bookmygift.service;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.entity.User;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.reqresp.PlaceOrderRequest;
import com.bookmygift.reqresp.ShowOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookMyGiftService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @SneakyThrows({JsonProcessingException.class, AmqpException.class})
    public Order placeOrder(PlaceOrderRequest orderRequest) {

        String username = orderRequest.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UnAuthorizedException(ErrorEnums.INVALID_CREDENTIALS));

        Order order = Order.builder().orderId(username.substring(0, 3).toUpperCase() + "_" + UUID.randomUUID())
                .username(username).emailId(user.getEmail()).giftType(GiftType.valueOf(orderRequest.getGiftType()))
                .amountPaid(orderRequest.getAmountPaid()).orderStatus(OrderStatus.ORDER_RECEIVED).build();

        orderRepository.save(order);

        rabbitTemplate.convertAndSend("directExchange", "orderRoutingKey", objectMapper.writeValueAsString(order));

        return order;

    }

    public List<Order> showMyOrders(ShowOrderRequest orderRequest) {

        String username = orderRequest.getUsername();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        Predicate usernamePredicate = criteriaBuilder.equal(root.get("username"), username);
        criteriaQuery.where(usernamePredicate);

        if (StringUtils.isNotBlank(String.valueOf(orderRequest.getGiftType()))) {
            Predicate giftTypePredicate = criteriaBuilder.equal(root.get("giftType"), orderRequest.getGiftType());
            criteriaQuery.where(criteriaBuilder.and(usernamePredicate, giftTypePredicate));
        }

        if (StringUtils.isNotBlank(String.valueOf(orderRequest.getOrderStatus()))) {
            Predicate orderStatusPredicate = criteriaBuilder.equal(root.get("orderStatus"), orderRequest.getOrderStatus());
            criteriaQuery.where(criteriaBuilder.and(usernamePredicate, orderStatusPredicate));
        }

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();

    }

    @SneakyThrows({JsonProcessingException.class, AmqpException.class})
    public Order cancelOrder(String orderId, String username) {

        Order order = orderRepository.findByOrderIdAndUsername(orderId, username).orElseThrow(() -> new BadRequestException(ErrorEnums.INVALID_ORDER_ID));

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        rabbitTemplate.convertAndSend("directExchange", "cancelRoutingKey", objectMapper.writeValueAsString(order));

        return order;

    }

}
