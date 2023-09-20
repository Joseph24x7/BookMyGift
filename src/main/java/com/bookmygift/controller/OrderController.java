package com.bookmygift.controller;

import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.response.OrderResponse;
import com.bookmygift.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService bookMyGiftService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody @Valid PlaceOrderRequest orderRequest, Authentication authentication) {
        log.debug("Entering placeOrder with PlaceOrderRequest: {}", orderRequest);
        orderRequest.setEmail(String.valueOf(authentication.getCredentials()));
        OrderResponse orderResponse = bookMyGiftService.placeOrder(orderRequest);
        log.debug("Exiting placeOrder with OrderResponse: {}", orderResponse);
        return orderResponse;
    }

    @GetMapping
    public OrderResponse showMyOrders(@ModelAttribute ShowOrderRequest orderRequest, Authentication authentication) {
        log.debug("Entering showMyOrders with ShowOrderRequest: {}", orderRequest);
        orderRequest.setEmail(String.valueOf(authentication.getCredentials()));
        OrderResponse orderResponse = bookMyGiftService.showMyOrders(orderRequest);
        log.debug("Exiting showMyOrders with OrderResponse: {}", orderResponse);
        return orderResponse;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponse cancelOrder(@RequestParam(name = "orderId") String orderId, Authentication authentication) {
        log.debug("Entering cancelOrder with OrderId: {}", orderId);
        OrderResponse orderResponse = bookMyGiftService.cancelOrder(orderId, String.valueOf(authentication.getCredentials()));
        log.debug("Exiting cancelOrder with OrderResponse: {}", orderResponse);
        return orderResponse;
    }
}
