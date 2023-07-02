package com.bookmygift.service;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.ServiceException;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.reqresp.OrderRequest;
import com.bookmygift.utils.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
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
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final RabbitTemplate rabbitTemplate;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @SneakyThrows({JsonProcessingException.class, AmqpException.class})
    public Order placeOrder(OrderRequest orderRequest) {

        String username = tokenGenerator.extractUsername(request.getHeader("Authorization").replace("Bearer ", ""));

        var user = userRepository.findByUsername(username).orElseThrow(() -> new ServiceException(ErrorEnums.UNAUTHORIZED));

        var order = Order.builder().orderId(username.substring(0, 3).toUpperCase() + "_" + UUID.randomUUID())
                .username(username).emailId(user.getEmail()).giftType(orderRequest.getGiftType())
                .amountPaid(orderRequest.getAmountPaid()).orderStatus(OrderStatus.ORDER_RECEIVED).build();

        orderRepository.save(order);

        rabbitTemplate.convertAndSend("directExchange", "orderRoutingKey", objectMapper.writeValueAsString(order));

        return order;

    }

    public List<Order> showMyOrders(GiftType giftType, OrderStatus orderStatus) {

        String username = tokenGenerator.extractUsername(request.getHeader("Authorization").replace("Bearer ", ""));
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

        var order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new ServiceException(ErrorEnums.INVALID_ORDER_ID));

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        rabbitTemplate.convertAndSend("directExchange", "cancelRoutingKey", objectMapper.writeValueAsString(order));

        return order;

    }

}
