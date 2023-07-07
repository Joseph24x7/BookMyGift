package com.bookmygift.controller;

import com.bookmygift.entity.Order;
import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.service.BookMyGiftService;
import com.bookmygift.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookMyGiftController {

    private final BookMyGiftService bookMyGiftService;
    private final TokenUtil tokenUtil;

    @PostMapping("/placeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(@RequestBody @Valid PlaceOrderRequest orderRequest, HttpServletRequest servletRequest) {
        String username = tokenUtil.extractUsernameFromRequest(servletRequest);
        orderRequest.setUsername(username);
        return bookMyGiftService.placeOrder(orderRequest);
    }

    @GetMapping("/showMyOrders")
    public List<Order> showMyOrders(@ModelAttribute ShowOrderRequest orderRequest, HttpServletRequest servletRequest) {
        String username = tokenUtil.extractUsernameFromRequest(servletRequest);
        orderRequest.setUsername(username);
        return bookMyGiftService.showMyOrders(orderRequest);
    }

    @DeleteMapping("/cancelOrder")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Order cancelOrder(@RequestParam(name = "orderId") String orderId, HttpServletRequest servletRequest) {
        String username = tokenUtil.extractUsernameFromRequest(servletRequest);
        return bookMyGiftService.cancelOrder(orderId, username);
    }
}
