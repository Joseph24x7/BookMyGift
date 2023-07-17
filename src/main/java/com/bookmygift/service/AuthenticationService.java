package com.bookmygift.service;

import com.bookmygift.entity.RoleEnum;
import com.bookmygift.entity.User;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.request.AuthRequest;
import com.bookmygift.request.VerifyRequest;
import com.bookmygift.response.AuthResponse;
import com.bookmygift.response.AuthenticationStatus;
import com.bookmygift.utils.ErrorEnums;
import com.bookmygift.utils.TokenUtil;
import com.bookmygift.utils.ValidatorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final AuthenticationManager authenticationManager;
    private final QueueService queueService;
    private final ValidatorUtil validatorUtil;

    public AuthResponse registerUser(AuthRequest authRequest) {

        String username = getUsernameFromEmail(authRequest.getEmail());

        userRepository.findByUsername(username).ifPresent(u -> {
            throw new BadRequestException(ErrorEnums.USER_ALREADY_REGISTERED);
        });

        User user = User.builder().username(username).password(passwordEncoder.encode(authRequest.getPassword())).email(authRequest.getEmail()).role(RoleEnum.CUSTOMER).fullName(authRequest.getFullName()).twoFaCode(generateTwoFaCode()).twoFaExpiry(getExpiryTimeForTwoFa()).build();

        validatorUtil.validate(user);

        userRepository.save(user);

        queueService.sendTwoFactorAuthentication(user);

        String jwtToken = tokenUtil.generateToken(user);

        return createAuthResponse(user, jwtToken);
    }

    public AuthResponse authenticateUser(AuthRequest authRequest, boolean isVerification) {

        String username = getUsernameFromEmail(authRequest.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UnAuthorizedException(ErrorEnums.INVALID_CREDENTIALS));

        if (!isVerification) {

            user.toBuilder().twoFaCode(generateTwoFaCode()).twoFaExpiry(getExpiryTimeForTwoFa()).build();

            queueService.sendTwoFactorAuthentication(user);

            userRepository.save(user);
        }

        String jwtToken = tokenUtil.generateToken(user);

        return createAuthResponse(user, jwtToken);
    }

    public AuthResponse verifyUser(VerifyRequest verifyRequest) {

        AuthResponse authResponse = authenticateUser(verifyRequest, true);

        User user = authResponse.getUser();

        if (user.isVerified()) {
            throw new BadRequestException(ErrorEnums.TWO_FA_ALREADY_VERIFIED);

        } else if (!(authResponse.getAuthenticationStatus().equals(AuthenticationStatus.SUCCESS) && user.getTwoFaCode().equalsIgnoreCase(verifyRequest.getTwoFaCode()))) {
            throw new UnAuthorizedException(ErrorEnums.INVALID_2FA_CODE);

        } else {

            userRepository.save(user.toBuilder().isVerified(Boolean.TRUE).build());

            queueService.sendVerificationSuccessNotification(authResponse.getUser());

            return authResponse;

        }
    }

    private String getUsernameFromEmail(String email) {
        return email.substring(0, email.indexOf('@'));
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

}
