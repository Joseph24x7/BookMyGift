package com.bookmyticket.security.info;

import lombok.Data;

@Data
public class AuthRequest {

	private String username;
	private String password;
	private String email;

}
