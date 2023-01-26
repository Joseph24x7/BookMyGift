package com.bookmygift.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.ServiceException;
import com.bookmygift.info.Role;
import com.bookmygift.info.User;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.security.TokenGenerator;
import com.bookmygift.security.info.AuthRequest;
import com.bookmygift.security.info.AuthResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository repository;

	private final PasswordEncoder passwordEncoder;

	private final TokenGenerator jwtService;

	private final AuthenticationManager authenticationManager;

	@Transactional
	public AuthResponse register(AuthRequest authInfo) {

		repository.findByUsername(authInfo.getUsername()).ifPresent(u -> {
			throw new ServiceException(ErrorEnums.USER_ALREADY_REGISTERED);
		});

		var user = User.builder().username(authInfo.getUsername())
				.password(passwordEncoder.encode(authInfo.getPassword())).email(authInfo.getEmail()).role(Role.CUSTOMER)
				.fullname(authInfo.getFullname()).build();

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
