package com.bookmyticket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookmyticket.JwtService;
import com.bookmyticket.advice.ErrorEnums;
import com.bookmyticket.advice.ServiceException;
import com.bookmyticket.info.AuthInfo;
import com.bookmyticket.info.AuthenticationResponse;
import com.bookmyticket.info.Role;
import com.bookmyticket.info.User;
import com.bookmyticket.repository.UserRepository;

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
	public AuthenticationResponse register(AuthInfo authInfo) {
		
		var user = User.builder().username(authInfo.getUsername()).password(passwordEncoder.encode(authInfo.getPassword())).email(authInfo.getEmail()).role(Role.USER).build();
		
		repository.save(user);
		
		var jwtToken = jwtService.generateToken(user);
		
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public AuthenticationResponse authenticate(AuthInfo authInfo) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authInfo.getUsername(), authInfo.getPassword()));
		
		var user = repository.findByUsername(authInfo.getUsername()).orElseThrow(() -> new ServiceException(ErrorEnums.UNAUTHORIZED));
		
		var jwtToken = jwtService.generateToken(user);
		
		return AuthenticationResponse.builder().token(jwtToken).build();
	}
}
