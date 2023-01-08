package com.bookmyticket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookmyticket.exception.ErrorEnums;
import com.bookmyticket.exception.ServiceException;
import com.bookmyticket.repository.UserRepository;
import com.bookmyticket.security.JwtService;
import com.bookmyticket.security.info.AuthRequest;
import com.bookmyticket.security.info.AuthResponse;
import com.bookmyticket.security.info.Role;
import com.bookmyticket.security.info.User;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Transactional
	public AuthResponse register(AuthRequest authInfo) {
		
		var user = User.builder().username(authInfo.getUsername()).password(passwordEncoder.encode(authInfo.getPassword())).email(authInfo.getEmail()).role(Role.USER).build();
		
		repository.save(user);
		
		var jwtToken = jwtService.generateToken(user);
		
		return AuthResponse.builder().token(jwtToken).build();
	}

	public AuthResponse authenticate(AuthRequest authInfo) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authInfo.getUsername(), authInfo.getPassword()));
		
		var user = repository.findByUsername(authInfo.getUsername()).orElseThrow(() -> new ServiceException(ErrorEnums.UNAUTHORIZED));
		
		var jwtToken = jwtService.generateToken(user);
		
		return AuthResponse.builder().token(jwtToken).build();
	}
}
