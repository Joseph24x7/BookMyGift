package com.bookmygift.security.info;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String email;

}
