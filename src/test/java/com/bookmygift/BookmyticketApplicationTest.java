package com.bookmygift;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.reqresp.AuthRequest;
import com.bookmygift.reqresp.AuthResponse;
import com.bookmygift.reqresp.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.Commit;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookmyticketApplicationTest {

    AuthRequest authRequest;
    HttpEntity<AuthRequest> requestEntity;
    HttpHeaders headers;
    OrderRequest orderRequest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void populateRequest() {

        authRequest = AuthRequest.builder().username("testUsername").password("Password123@").email("test@email.com").fullname("TestName").build();

        requestEntity = new HttpEntity<>(authRequest);

        headers = new HttpHeaders();

        try {
            headers.setBearerAuth(testRestTemplate.postForEntity("/api/v1/auth/authenticate", requestEntity, AuthResponse.class).getBody().getToken());
        } catch (Exception e) {
            headers.setBearerAuth(testRestTemplate.postForEntity("/api/v1/auth/register", requestEntity, AuthResponse.class).getBody().getToken());
        }

        orderRequest = OrderRequest.builder().giftType(GiftType.KEYCHAIN).amountPaid(300.0D).build();

    }

    @Test
    @Commit
    void bookMyGiftTestPostiveScenarios() {

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<Order> placeOrderResponse = testRestTemplate.exchange("/placeOrder", HttpMethod.POST, new HttpEntity<>(orderRequest, headers), Order.class);
        assertEquals(HttpStatus.CREATED, placeOrderResponse.getStatusCode());

        String urlGet = "/showMyOrders?giftType=" + GiftType.KEYCHAIN + "&orderStatus=" + OrderStatus.ORDER_RECEIVED;
        ResponseEntity<String> getOrder = testRestTemplate.exchange(urlGet, HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, getOrder.getStatusCode());

        String urlCancel = "/cancelOrder?orderId=" + placeOrderResponse.getBody().getOrderId();
        ResponseEntity<Order> cancelOrderResponse = testRestTemplate.exchange(urlCancel, HttpMethod.DELETE, request, Order.class);
        assertEquals(HttpStatus.ACCEPTED, cancelOrderResponse.getStatusCode());

        userRepository.deleteById(userRepository.findByUsername(authRequest.getUsername()).orElseThrow().getUserId());
        orderRepository.deleteById(placeOrderResponse.getBody().getOrderId());

    }

}
