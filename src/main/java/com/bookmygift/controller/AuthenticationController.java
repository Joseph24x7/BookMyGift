package com.bookmygift.controller;

import com.bookmygift.reqresp.AuthRequest;
import com.bookmygift.reqresp.AuthResponse;
import com.bookmygift.reqresp.VerifyRequest;
import com.bookmygift.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public AuthResponse register(@RequestBody @Valid AuthRequest authRequest) {
		return authenticationService.registerUser(authRequest);
	}

	@PostMapping("/authenticate")
	public AuthResponse authenticate(@RequestBody @Valid AuthRequest authRequest) {
		return authenticationService.authenticate(authRequest, false);
	}

	@PostMapping("/verify")
	public AuthResponse verify(@RequestBody @Valid VerifyRequest authRequest) {
		return authenticationService.verify(authRequest);
	}

}
