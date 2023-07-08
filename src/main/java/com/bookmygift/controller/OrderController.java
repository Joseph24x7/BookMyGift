package com.bookmygift.controller;

import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.response.OrderResponse;
import com.bookmygift.service.OrderService;
import com.bookmygift.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService bookMyGiftService;
    private final TokenUtil tokenUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody @Valid PlaceOrderRequest orderRequest, HttpServletRequest servletRequest) {
        log.debug("Getting Place Order Request: {}", orderRequest);
        orderRequest.setUsername(tokenUtil.extractUsernameFromRequest(servletRequest));
        OrderResponse orderResponse = bookMyGiftService.placeOrder(orderRequest);
        log.debug("Returning Place Order Response: {}", orderResponse);
        return orderResponse;
    }

    @GetMapping
    public OrderResponse showMyOrders(@ModelAttribute ShowOrderRequest orderRequest, HttpServletRequest servletRequest) {
        log.debug("Getting Show Order Request: {}", orderRequest);
        orderRequest.setUsername(tokenUtil.extractUsernameFromRequest(servletRequest));
        OrderResponse orderResponse = bookMyGiftService.showMyOrders(orderRequest);
        log.debug("Returning Show Order Response: {}", orderResponse);
        return orderResponse;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponse cancelOrder(@RequestParam(name = "orderId") String orderId, HttpServletRequest servletRequest) {
        log.debug("Getting cancel Order Id: {}", orderId);
        String username = tokenUtil.extractUsernameFromRequest(servletRequest);
        OrderResponse orderResponse = bookMyGiftService.cancelOrder(orderId, username);
        log.debug("Returning cancel Order Request: {}", orderResponse);
        return orderResponse;
    }
}
