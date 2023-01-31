package com.bookmygift.request;

import com.bookmygift.info.GiftType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

	@NotNull(message = "giftType should not be empty")
	private GiftType giftType;

	@NotNull(message = "amountPaid should not be empty")
	private Double amountPaid;
}
