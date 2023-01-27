package com.bookmygift.request;

import com.bookmygift.info.GiftType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

	@NotBlank(message = "username should not be empty")
	@Size(min = 3, max = 20, message = "username should be between 3 and 20 characters")
	private String username;

	@NotBlank(message = "emailId should not be empty")
	@Email(message = "emailId should be a valid email address")
	private String emailId;

	@NotNull(message = "giftType should not be empty")
	private GiftType giftType;

	@NotNull(message = "amountPaid should not be empty")
	private Double amountPaid;
}
