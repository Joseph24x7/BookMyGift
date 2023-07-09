package com.bookmygift.controller;

import com.bookmygift.request.AuthRequest;
import com.bookmygift.request.VerifyRequest;
import com.bookmygift.response.AuthResponse;
import com.bookmygift.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthResponse registerUser(@RequestBody @Valid AuthRequest authRequest) {
        log.debug("Entering registerUser with AuthRequest: {}", authRequest);
        AuthResponse authResponse = authenticationService.registerUser(authRequest);
        log.debug("Exiting registerUser with AuthResponse: {}", authResponse);
        return authResponse;
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticateUser(@RequestBody @Valid AuthRequest authRequest) {
        log.debug("Entering authenticateUser with AuthRequest: {}", authRequest);
        AuthResponse authResponse = authenticationService.authenticateUser(authRequest, false);
        log.debug("Exiting authenticateUser with AuthResponse: {}", authResponse);
        return authResponse;
    }

    @PostMapping("/verify")
    public AuthResponse verifyUser(@RequestBody @Valid VerifyRequest verifyRequest) {
        log.debug("Entering verifyUser with VerifyRequest: {}", verifyRequest);
        AuthResponse authResponse = authenticationService.verifyUser(verifyRequest);
        log.debug("Exiting verifyUser with AuthResponse: {}", authResponse);
        return authResponse;
    }

}
