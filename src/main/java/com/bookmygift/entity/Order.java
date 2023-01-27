package com.bookmygift.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bookmygift.info.GiftType;
import com.bookmygift.info.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "OrderInfo")
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String orderId;

	@Indexed(unique = true)
	private String username;

	private String emailId;

	private GiftType giftType;

	private Double amountPaid;

	private OrderStatus orderStatus;

	private LocalDateTime expectedTimeToBeDelivered;

}