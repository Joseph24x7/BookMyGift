package com.bookmygift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bookmygift.entity.Role;
import com.bookmygift.entity.User;
import com.bookmygift.exception.ServiceException;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.reqresp.AuthRequest;
import com.bookmygift.reqresp.AuthResponse;
import com.bookmygift.utils.CommonUtils;
import com.bookmygift.utils.TokenGenerator;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class AuthenticationServiceTest {

	@Mock
	private UserRepository repository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private TokenGenerator jwtService;

	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private CommonUtils commonUtils;
	
	@InjectMocks
	private AuthenticationService authenticationService;

	AuthRequest authRequest;
	User user;

	@BeforeEach
	void populateRequest() {

		authRequest = AuthRequest.builder().username("username").password("Password123@").email("user@email.com")
				.fullname("tommy").build();

		user = User.builder().username("john").password("encodedPassword").email("john@example.com").role(Role.CUSTOMER)
				.fullname("John Doe").build();
	}

	@Test
	void testRegister() {

		when(repository.findByUsernameAndEmail(authRequest.getUsername(),authRequest.getEmail())).thenReturn(Optional.empty());
		
		when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("encodedPassword");
		
		when(jwtService.generateToken(Mockito.any())).thenReturn("jwtToken");
		
		AuthResponse authResponse = authenticationService.register(authRequest);
	    
	    assertEquals("jwtToken", authResponse.getToken());

	}
	
	@Test
	void testRegisterInvalid() {

		when(repository.findByUsernameAndEmail(authRequest.getUsername(),authRequest.getEmail())).thenReturn(Optional.of(user));
		
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
