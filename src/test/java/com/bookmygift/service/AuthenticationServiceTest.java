package com.bookmygift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bookmygift.exception.ServiceException;
import com.bookmygift.info.Role;
import com.bookmygift.info.User;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.security.TokenGenerator;
import com.bookmygift.security.info.AuthRequest;
import com.bookmygift.security.info.AuthResponse;

@SpringBootTest
class AuthenticationServiceTest {

	@Mock
	private UserRepository repository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private TokenGenerator jwtService;

	@Mock
	private AuthenticationManager authenticationManager;
	
	@InjectMocks
	private AuthenticationService authenticationService;

	AuthRequest authRequest;
	User user;

	@BeforeEach
	void populateRequest() {

		authRequest = AuthRequest.builder().username("username").password("password").email("user@email.com")
				.fullname("tommy").build();

		user = User.builder().username("john").password("encodedPassword").email("john@example.com").role(Role.CUSTOMER)
				.fullname("John Doe").build();
	}

	@Test
	void testRegister() {

		when(repository.findByUsername(authRequest.getUsername())).thenReturn(Optional.empty());
		
		when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("encodedPassword");
		
		when(jwtService.generateToken(Mockito.any())).thenReturn("jwtToken");
		
		AuthResponse authResponse = authenticationService.register(authRequest);
	    
	    assertEquals("jwtToken", authResponse.getToken());

	}
	
	@Test
	void testRegisterInvalid() {

		when(repository.findByUsername(authRequest.getUsername())).thenReturn(Optional.of(user));
		
		assertThrows(ServiceException.class, () -> authenticationService.register(authRequest));
	    
	}
	
	@Test
	void testAuthenticate() {
		
		when(repository.findByUsername(authRequest.getUsername())).thenReturn(Optional.of(user));
		
		Authentication auth = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
		
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()))).thenReturn(auth);
		
		when(jwtService.generateToken(Mockito.any())).thenReturn("jwtToken");
		
		AuthResponse authResponse = authenticationService.authenticate(authRequest);
	    
	    assertEquals("jwtToken", authResponse.getToken());

	}
	
}
