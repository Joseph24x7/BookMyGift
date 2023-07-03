package com.bookmygift.controller;

import com.bookmygift.entity.Order;
import com.bookmygift.reqresp.OrderRequest;
import com.bookmygift.service.BookMyGiftService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Book My Gift Management", version = "0.0.1"))
@RequiredArgsConstructor
public class BookMyGiftController {

    private final BookMyGiftService bookMyGiftService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return new ResponseEntity<>(bookMyGiftService.placeOrder(orderRequest), HttpStatus.CREATED);
    }

    @GetMapping("/showMyOrders")
    public List<Order> showMyOrders(@ModelAttribute OrderRequest orderRequest) {
        return bookMyGiftService.showMyOrders(orderRequest.getGiftType(), orderRequest.getOrderStatus());
    }

    @DeleteMapping("/cancelOrder")
    public ResponseEntity<Order> cancelOrder(@ModelAttribute OrderRequest orderRequest) {
        return new ResponseEntity<>(bookMyGiftService.cancelOrder(orderRequest.getOrderId()), HttpStatus.ACCEPTED);
    }
}
