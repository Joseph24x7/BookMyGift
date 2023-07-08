package com.bookmygift.response;

import com.bookmygift.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Data
@Builder
@JsonInclude(value = Include.NON_EMPTY)
public class AuthResponse {

	@ToString.Exclude
	private String token;
	private AuthenticationStatus authenticationStatus;
	private UserEntity user;

}
