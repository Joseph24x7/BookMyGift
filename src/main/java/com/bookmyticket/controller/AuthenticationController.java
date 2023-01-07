
package com.bookmyticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookmyticket.TokenGenerator;
import com.bookmyticket.info.AuthInfo;

@RestController
public class AuthenticationController {

	@Autowired
	private TokenGenerator tokenGenerator;

	@PostMapping("/authenticate")
	public String generateToken(@RequestBody AuthInfo authInfo) throws Exception {
		return tokenGenerator.generateToken(authInfo.getUserName());
	}

}
