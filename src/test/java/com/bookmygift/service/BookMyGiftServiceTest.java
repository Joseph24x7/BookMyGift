
package com.bookmygift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.entity.User;
import com.bookmygift.exception.ServiceException;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.reqresp.OrderRequest;
import com.bookmygift.utils.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BookMyGiftServiceTest {

	@InjectMocks
	private BookMyGiftService bookMyGiftService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private MongoTemplate mongoTemplate;

	@Mock
	private TokenGenerator tokenGenerator;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RabbitTemplate rabbitTemplate;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private HttpServletRequest request;

	OrderRequest orderRequest;
	Order order;
	String orderId;

	@BeforeEach
	void populateRequestResponse() {

		orderRequest = OrderRequest.builder().giftType(GiftType.KEYCHAIN).amountPaid(100.0D).build();

		order = Order.builder().orderId("USE_" + UUID.randomUUID()).username("username").emailId("email@email.com")
				.giftType(GiftType.FRAME).amountPaid(100.0D).orderStatus(OrderStatus.ORDER_RECIEVED).build();

		orderId = "ORDER_ID";

	}

	@Test
	void placeOrderTest() {
		
		when(request.getHeader(anyString())).thenReturn("Bearer some_token");
		
		when(tokenGenerator.extractUsername(anyString())).thenReturn("test_user");
		
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(new User()));
		
		Order order =bookMyGiftService.placeOrder(orderRequest);

		assertEquals(order.getGiftType(), bookMyGiftService.placeOrder(orderRequest).getGiftType());
	}

	@Test
	void showMyOrdersTest() throws Exception {

		when(request.getHeader(anyString())).thenReturn("Bearer some_token");
		
		when(tokenGenerator.extractUsername(anyString())).thenReturn("test_user");

		List<Order> expectedOrders = List.of(order);

		when(mongoTemplate.find(Mockito.any(Query.class), eq(Order.class))).thenReturn(expectedOrders);

		List<Order> actualOrders = bookMyGiftService.showMyOrders(GiftType.KEYCHAIN, OrderStatus.ORDER_RECIEVED);

		assertEquals(expectedOrders, actualOrders);

	}

	@Test
	void cancelOrderTest() {

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		Order result = bookMyGiftService.cancelOrder(orderId);

		assertEquals(OrderStatus.CANCELLED, result.getOrderStatus());

	}

	@Test
	void cancelOrderInvalidIdTest() {

		when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> bookMyGiftService.cancelOrder(orderId));
	}

}
