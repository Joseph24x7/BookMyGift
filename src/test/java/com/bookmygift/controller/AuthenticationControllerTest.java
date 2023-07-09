package com.bookmygift.controller;

import com.bookmygift.request.AuthRequest;
import com.bookmygift.request.VerifyRequest;
import com.bookmygift.response.AuthResponse;
import com.bookmygift.response.AuthenticationStatus;
import com.bookmygift.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private AuthRequest authRequest;
    private AuthResponse authResponse;
    private VerifyRequest verifyRequest;

    @BeforeEach
    void populateRequest() {
        authResponse = AuthResponse.builder().token("dummyToken").authenticationStatus(AuthenticationStatus.SUCCESS).build();

        authRequest = new AuthRequest();
        authRequest.setPassword("Password123@");
        authRequest.setEmail("user@email.com");
        authRequest.setFullName("tommy");

        verifyRequest = new VerifyRequest();
        verifyRequest.setPassword("Password123@");
        verifyRequest.setEmail("user@email.com");
        verifyRequest.setFullName("tommy");
        verifyRequest.setTwoFaCode("twoFaCode");
    }

    @Test
    void register() {
        when(authenticationService.registerUser(authRequest)).thenReturn(authResponse);
        AuthResponse actualResponse = authenticationController.registerUser(authRequest);
        assertEquals(authResponse, actualResponse);
    }

    @Test
    void authenticate() {
        when(authenticationService.authenticateUser(authRequest, false)).thenReturn(authResponse);
        AuthResponse actualResponse = authenticationController.authenticateUser(authRequest);
        assertEquals(authResponse, actualResponse);
    }

    @Test
    void verify() {
        when(authenticationService.verifyUser(verifyRequest)).thenReturn(authResponse);
        AuthResponse actualResponse = authenticationController.verifyUser(verifyRequest);
        assertEquals(authResponse, actualResponse);
    }
}
