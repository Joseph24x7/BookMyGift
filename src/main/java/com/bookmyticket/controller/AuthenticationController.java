
package com.bookmyticket.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmyticket.security.info.AuthRequest;
import com.bookmyticket.security.info.AuthResponse;
import com.bookmyticket.service.AuthenticationService;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@OpenAPIDefinition(info = @Info(title = "Authentication Management", version = "0.0.1"))
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final ObservationRegistry observationRegistry;

	@PostMapping("/register")
	public AuthResponse register(@RequestBody AuthRequest authRequest, HttpServletRequest request) {

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> authenticationService.register(authRequest));

	}

	@PostMapping("/authenticate")
	public AuthResponse authenticate(@RequestBody AuthRequest authRequest, HttpServletRequest request) {

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> authenticationService.authenticate(authRequest));

	}

}
