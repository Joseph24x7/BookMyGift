package com.bookmygift.service;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class BookMyGiftService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	
	private final TokenGenerator tokenGenerator;

	private final RabbitTemplate rabbitTemplate;
	private final MongoTemplate mongoTemplate;
	
	private final ObjectMapper objectMapper;
	private final HttpServletRequest request;

	@SneakyThrows({JsonProcessingException.class, AmqpException.class})
	public Order placeOrder(OrderRequest orderRequest) {

		String username = tokenGenerator.extractUsername(request.getHeader("Authorization").replace("Bearer ", ""));
		
		var user = userRepository.findByUsername(username).orElseThrow(() -> new ServiceException(ErrorEnums.UNAUTHORIZED));

		var order = Order.builder().orderId(username.substring(0, 3).toUpperCase() + "_" + UUID.randomUUID())
				.username(username).emailId(user.getEmail()).giftType(orderRequest.getGiftType())
				.amountPaid(orderRequest.getAmountPaid()).orderStatus(OrderStatus.ORDER_RECIEVED).build();

		orderRepository.save(order);

		rabbitTemplate.convertAndSend("directExchange", "orderRoutingKey", objectMapper.writeValueAsString(order));

		return order;

	}

	public List<Order> showMyOrders(GiftType giftType, OrderStatus orderStatus) {

		Query query = new Query();

		Criteria criteria = new Criteria();

		criteria.and("username")
				.is(tokenGenerator.extractUsername(request.getHeader("Authorization").replace("Bearer ", "")));

		if (giftType != null && !EnumSet.of(giftType).isEmpty())
			criteria.and("giftType").is(giftType);

		if (orderStatus != null && !EnumSet.of(orderStatus).isEmpty())
			criteria.and("orderStatus").is(orderStatus);

		query.addCriteria(criteria);

		return mongoTemplate.find(query, Order.class);

	}

	@SneakyThrows({JsonProcessingException.class, AmqpException.class})
	public Order cancelOrder(String orderId) {

		var order = orderRepository.findById(orderId).orElseThrow(() -> new ServiceException(ErrorEnums.INVALID_ORDER_ID));

		order.setOrderStatus(OrderStatus.CANCELLED);

		orderRepository.save(order);

		rabbitTemplate.convertAndSend("directExchange", "cancelRoutingKey", objectMapper.writeValueAsString(order));

		return order;

	}

}
