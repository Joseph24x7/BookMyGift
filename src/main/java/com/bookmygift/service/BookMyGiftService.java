package com.bookmygift.service;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bookmygift.entity.Order;
import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.ServiceException;
import com.bookmygift.exception.UncheckedFunction;
import com.bookmygift.info.GiftType;
import com.bookmygift.info.OrderStatus;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.request.OrderRequest;
import com.bookmygift.security.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookMyGiftService {

	private final OrderRepository orderRepository;

	private final TokenGenerator tokenGenerator;

	private final RabbitTemplate rabbitTemplate;
	private final MongoTemplate mongoTemplate;
	private final ObjectMapper objectMapper;

	public Order placeOrder(OrderRequest orderRequest) {

		var order = Order.builder()
				.orderId(orderRequest.getUsername().substring(0, 3).toUpperCase() + "_" + UUID.randomUUID())
				.username(orderRequest.getUsername()).emailId(orderRequest.getEmailId())
				.giftType(orderRequest.getGiftType()).amountPaid(orderRequest.getAmountPaid())
				.orderStatus(OrderStatus.ORDER_RECIEVED)
				.build();

		orderRepository.save(order);

		String orderJson= UncheckedFunction.unchecked(t -> objectMapper.writeValueAsString(order)).apply(order);

		rabbitTemplate.convertAndSend("directExchange", "orderRoutingKey", orderJson);

		return order;

	}

	public List<Order> showMyOrders(HttpServletRequest request, GiftType giftType, OrderStatus orderStatus) {

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

	public Order cancelOrder(String orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ServiceException(ErrorEnums.INVALID_ORDER_ID));

		order.setOrderStatus(OrderStatus.CANCELLED);
		
		orderRepository.save(order);

		String orderJson= UncheckedFunction.unchecked(t -> objectMapper.writeValueAsString(order)).apply(order);

		rabbitTemplate.convertAndSend("directExchange", "cancelRoutingKey", orderJson);

		return order;

	}

}
