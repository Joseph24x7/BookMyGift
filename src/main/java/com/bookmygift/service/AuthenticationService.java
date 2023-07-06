package com.bookmygift.service;

import com.bookmygift.entity.Role;
import com.bookmygift.entity.User;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.UnAuthorizedException;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final AuthenticationManager authenticationManager;
    private final RabbitTemplate rabbitTemplate;
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

        AuthResponse authResponse = authenticate(verifyRequest, true);

        if (!(authResponse.getAuthenticationStatus().equals(AuthenticationStatus.SUCCESS) && authResponse.getUser().getTwoFaCode().equalsIgnoreCase(verifyRequest.getTwoFaCode()))) {
            throw new UnAuthorizedException(ErrorEnums.INVALID_2FA_CODE);
        }

        sendSuccessNotification(authResponse);

        return authResponse;
    }

    @SneakyThrows({JsonProcessingException.class})
    private void sendSuccessNotification(AuthResponse authResponse) {
        rabbitTemplate.convertAndSend("directExchange", "sendVerifySuccessRoutingKey", objectMapper.writeValueAsString(authResponse.getUser()));
    }

    private User validateUser(AuthRequest authRequest) {

        String username = getUsernameFromEmail(authRequest.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

        return userRepository.findByUsername(username).orElseThrow(() -> new UnAuthorizedException(ErrorEnums.INVALID_CREDENTIALS));

    }

    private String getUsernameFromEmail(String email) {
        return email.substring(0, email.indexOf('@'));
    }

    private void checkIfUsernameExists(String username) {
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new BadRequestException(ErrorEnums.USER_ALREADY_REGISTERED);
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

        User user = User.builder().username(username).password(passwordEncoder.encode(authRequest.getPassword())).email(authRequest.getEmail()).role(Role.CUSTOMER).fullName(authRequest.getFullName()).twoFaCode(generateTwoFaCode()).twoFaExpiry(getExpiryTimeForTwoFa()).build();

        validate(user);

        userRepository.save(user);

        return user;
    }

}
