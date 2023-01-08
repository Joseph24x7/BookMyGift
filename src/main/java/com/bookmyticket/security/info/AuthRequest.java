package com.bookmyticket.security.info;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

	private String username;
	private String password;
	private String email;

}
