package com.bookmygift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatus;
import com.bookmygift.reqresp.OrderRequest;
import com.bookmygift.service.BookMyGiftService;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@OpenAPIDefinition(info = @Info(title = "Theatre Management", version = "0.0.1"))
@RequiredArgsConstructor
public class BookMyGiftController {

	private final BookMyGiftService bookMyGiftService;
	private final ObservationRegistry observationRegistry;

	@PostMapping("/placeOrder")
	public ResponseEntity<Order> placeOrder(@RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request) {
		
		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> new ResponseEntity<>(bookMyGiftService.placeOrder(orderRequest), HttpStatus.CREATED));

	}

	@GetMapping("/showMyOrders")
	public List<Order> showMyOrders(@RequestParam(value = "giftType", required = false) GiftType giftType,
			@RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus,
			HttpServletRequest request) {

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> bookMyGiftService.showMyOrders(giftType, orderStatus));

	}

	@DeleteMapping("/cancelOrder")
	public ResponseEntity<Order> cancelOrder(@RequestParam(value = "orderId", required = true) String orderId, HttpServletRequest request) {

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> new ResponseEntity<>(bookMyGiftService.cancelOrder(orderId), HttpStatus.ACCEPTED));
	}
}
