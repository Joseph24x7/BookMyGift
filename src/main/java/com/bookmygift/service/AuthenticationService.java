package com.bookmygift.service;

import com.bookmygift.entity.Role;
import com.bookmygift.entity.User;
import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.ServiceException;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.reqresp.AuthRequest;
import com.bookmygift.reqresp.AuthResponse;
import com.bookmygift.reqresp.AuthenticationStatus;
import com.bookmygift.reqresp.VerifyRequest;
import com.bookmygift.utils.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenUtil tokenUtil;
	private final AuthenticationManager authenticationManager;
	private final RabbitTemplate rabbitTemplate;
	private final KafkaTemplate<String, User> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final Validator validator;

	public AuthResponse registerUser(AuthRequest authRequest) {

		String username = getUsernameFromEmail(authRequest.getEmail());

		checkIfUsernameExists(username);

		User user = createUser(authRequest, username);

		sendOtp(user);

		String jwtToken = tokenUtil.generateToken(user);

		return createAuthResponse(user, jwtToken);
	}

	public AuthResponse authenticate(AuthRequest authRequest, boolean isVerification) {

		User user = validateUser(authRequest);

		if (!isVerification) {

			user.toBuilder().twoFaCode(generateTwoFaCode()).twoFaExpiry(getExpiryTimeForTwoFa()).build();

			sendOtp(user);

			userRepository.save(user);
		}

		String jwtToken = tokenUtil.generateToken(user);

		return createAuthResponse(user, jwtToken);
	}

	public AuthResponse verify(VerifyRequest verifyRequest) {

		CompletableFuture<AuthResponse> completableFuture = new CompletableFuture<>();

		try {

			// runAsync is used to produce a result asynchronously
			CompletableFuture<Void> authCheckFuture = authenticateTwoFaAsync(completableFuture, verifyRequest);

			CompletableFuture.allOf(authCheckFuture).get();

			AuthResponse authResponse = completableFuture.get();

			sendSuccessNotification(authResponse, authCheckFuture);

			return authResponse;

		} catch (InterruptedException | ExecutionException e) {

			Thread.currentThread().interrupt();
			if (e.getCause() instanceof ServiceException ex) {
				throw new ServiceException(ex.getErrorEnums());
			} else if (e.getCause() instanceof BadCredentialsException ex) {
				throw ex;
			}

			throw new ServiceException(ErrorEnums.INTERNAL_SERVER_ERROR);
		}
	}

	private void sendSuccessNotification(AuthResponse authResponse, CompletableFuture<Void> authCheckFuture) {

		authCheckFuture.thenApplyAsync(result -> {
			try {
				return kafkaTemplate.send("mytopic", authResponse.getUser()).get();
			} catch (InterruptedException | ExecutionException e) {
				Thread.currentThread().interrupt();
				throw new ServiceException(ErrorEnums.INTERNAL_SERVER_ERROR);
			}
		});

	}

	private CompletableFuture<Void> authenticateTwoFaAsync(CompletableFuture<AuthResponse> completableFuture,
														   VerifyRequest verifyRequest) {

		return CompletableFuture.runAsync(() -> {
			AuthResponse authResponse = authenticate(verifyRequest, true);
			if (authResponse.getAuthenticationStatus().equals(AuthenticationStatus.SUCCESS)
					&& authResponse.getUser().getTwoFaCode().equalsIgnoreCase(verifyRequest.getTwoFaCode())) {
				completableFuture.complete(authResponse);
			} else {
				completableFuture.completeExceptionally(new ServiceException(ErrorEnums.INVALID_2FA_CODE));
			}
		});

	}

	private User validateUser(AuthRequest authRequest) {

		String username = getUsernameFromEmail(authRequest.getEmail());

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

		return userRepository.findByUsername(username).orElseThrow(() -> new ServiceException(ErrorEnums.INVALID_CREDENTIALS));

	}

	private String getUsernameFromEmail(String email) {
		return email.substring(0, email.indexOf('@'));
	}

	private void checkIfUsernameExists(String username) {
		userRepository.findByUsername(username).ifPresent(u -> {
			throw new ServiceException(ErrorEnums.EMAIL_ALREADY_REGISTERED);
		});
	}

	private void validate(Object object) {
		Set<ConstraintViolation<Object>> violations = validator.validate(object);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	@SneakyThrows({JsonProcessingException.class})
	private void sendOtp(User user) {
		rabbitTemplate.convertAndSend("directExchange", "sendOtpRoutingKey", objectMapper.writeValueAsString(user));
	}

	private String generateTwoFaCode() {
		return "B-" + (new Random().nextInt(900000) + 100000);
	}

	private String getExpiryTimeForTwoFa() {
		return ZonedDateTime.now().plusMinutes(10).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	private AuthResponse createAuthResponse(User user, String jwtToken) {
		return AuthResponse.builder().token(jwtToken).user(user).authenticationStatus(AuthenticationStatus.SUCCESS).build();
	}

	private User createUser(AuthRequest authRequest, String username) {

		User user = User.builder().username(username).password(passwordEncoder.encode(authRequest.getPassword()))
				.email(authRequest.getEmail()).role(Role.CUSTOMER).fullName(authRequest.getFullName())
				.twoFaCode(generateTwoFaCode()).twoFaExpiry(getExpiryTimeForTwoFa()).build();

		validate(user);
		userRepository.save(user);

		return user;
	}

}
