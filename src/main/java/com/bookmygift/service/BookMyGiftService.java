package com.bookmygift.service;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.reqresp.OrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @SneakyThrows({JsonProcessingException.class, AmqpException.class})
    public Order placeOrder(OrderRequest orderRequest) {

        String username = "dummy";

        //var user = userRepository.findByUsername(username).orElseThrow(() -> new ServiceException(ErrorEnums.UNAUTHORIZED));

        var order = Order.builder().orderId(username.substring(0, 3).toUpperCase() + "_" + UUID.randomUUID())
                .username(username).emailId(username).giftType(orderRequest.getGiftType())
                .amountPaid(orderRequest.getAmountPaid()).orderStatus(OrderStatus.ORDER_RECEIVED).build();

        orderRepository.save(order);

        rabbitTemplate.convertAndSend("directExchange", "orderRoutingKey", objectMapper.writeValueAsString(order));

        return order;

    }

    public List<Order> showMyOrders(GiftType giftType, OrderStatus orderStatus) {

        String username = "dummy";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        Predicate usernamePredicate = criteriaBuilder.equal(root.get("username"), username);
        criteriaQuery.where(usernamePredicate);

        if (giftType != null) {
            Predicate giftTypePredicate = criteriaBuilder.equal(root.get("giftType"), giftType);
            criteriaQuery.where(criteriaBuilder.and(usernamePredicate, giftTypePredicate));
        }

        if (orderStatus != null) {
            Predicate orderStatusPredicate = criteriaBuilder.equal(root.get("orderStatus"), orderStatus);
            criteriaQuery.where(criteriaBuilder.and(usernamePredicate, orderStatusPredicate));
        }

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();

    }

    @SneakyThrows({JsonProcessingException.class, AmqpException.class})
    public Order cancelOrder(String orderId) {

        var order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new BadRequestException(ErrorEnums.INVALID_ORDER_ID));

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        rabbitTemplate.convertAndSend("directExchange", "cancelRoutingKey", objectMapper.writeValueAsString(order));

        return order;

    }

}
