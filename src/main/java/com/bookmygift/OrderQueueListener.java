package com.bookmygift;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.bookmygift.entity.Order;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderQueueListener {

	private final EmailService emailService;

	@RabbitListener(queues = "orderQueue", containerFactory = "rabbitListenerContainerFactory")
	public void handleOrderMessage(Order order) {
		emailService.sendOrderConfirmationEmail(order);
	}

	@RabbitListener(queues = "cancelQueue", containerFactory = "rabbitListenerContainerFactory")
	public void handleCancelMessage(Order order) {
		emailService.sendOrderConfirmationEmail(order);
	}
}
