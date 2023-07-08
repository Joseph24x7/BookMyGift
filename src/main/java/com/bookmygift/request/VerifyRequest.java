package com.bookmygift.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyRequest extends AuthRequest {

	@NotBlank(message = "twoFaCode is required")
	@Pattern(regexp = "^B-\\d{6}$", message = "twoFaCode should be in the format 'B-XXXXXX', where X represents a 6-digit random number")
	@ToString.Exclude
	private String twoFaCode;

}
