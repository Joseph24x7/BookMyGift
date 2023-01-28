
package com.bookmygift;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;
import org.springframework.util.MultiValueMap;

import com.bookmygift.entity.Order;
import com.bookmygift.info.GiftType;
import com.bookmygift.info.OrderStatus;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.request.OrderRequest;
import com.bookmygift.security.info.AuthRequest;
import com.bookmygift.security.info.AuthResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookmyticketApplicationTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	AuthRequest authRequest;
	HttpEntity<AuthRequest> requestEntity;
	HttpHeaders headers;
	OrderRequest orderRequest;
	
	@BeforeEach
	void populateRequest() {
		
		authRequest = AuthRequest.builder().username("testUsername").password("testPassword").email("test@email.com").fullname("TestName").build();
		
		requestEntity = new HttpEntity<>(authRequest);
		
		headers = new HttpHeaders();
		
		try {
			headers.setBearerAuth(testRestTemplate.postForEntity("/api/v1/auth/authenticate", requestEntity, AuthResponse.class).getBody().getToken());
		}catch(Exception e) {
			headers.setBearerAuth(testRestTemplate.postForEntity("/api/v1/auth/register", requestEntity, AuthResponse.class).getBody().getToken());
		}
		
		orderRequest = new OrderRequest();
		orderRequest.setUsername("Tommy");
		orderRequest.setGiftType(GiftType.KEYCHAIN);
		orderRequest.setAmountPaid(300.0D);
		orderRequest.setEmailId("Tommy@gmail.com");

	}

	@Test
	@Commit
	void bookMyGiftTestPostiveScenarios() {
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

		ResponseEntity<Order> placeOrderResponse = testRestTemplate.exchange("/placeOrder", HttpMethod.POST, new HttpEntity<>(orderRequest,headers), Order.class);
		assertEquals(HttpStatus.CREATED, placeOrderResponse.getStatusCode());
		
		String urlGet = "/showMyOrders?giftType=" + GiftType.KEYCHAIN.toString() + "&orderStatus=" + OrderStatus.ORDER_RECIEVED.toString();
		ResponseEntity<String> getOrder = testRestTemplate.exchange(urlGet, HttpMethod.GET, request, String.class);
		assertEquals(HttpStatus.OK, getOrder.getStatusCode());
		
		String urlCancel = "/cancelOrder?orderId=" + placeOrderResponse.getBody().getOrderId();
		ResponseEntity<Order> cancelOrderResponse = testRestTemplate.exchange(urlCancel, HttpMethod.DELETE, request, Order.class);
		assertEquals(HttpStatus.ACCEPTED, cancelOrderResponse.getStatusCode());
		
		userRepository.deleteById(userRepository.findByUsername(authRequest.getUsername()).orElseThrow().getUserId());
		orderRepository.deleteById(placeOrderResponse.getBody().getOrderId());
		
	}

}
