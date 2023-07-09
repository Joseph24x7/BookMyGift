package com.bookmygift.service;

import com.bookmygift.entity.RoleEnum;
import com.bookmygift.entity.UserEntity;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private QueueService queueService;

    @Mock
    private ValidatorUtil validatorUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AuthRequest authRequest;
    private VerifyRequest verifyRequest;

    @BeforeEach
    public void setup() {
        authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");
        authRequest.setFullName("Test User");

        verifyRequest = new VerifyRequest();
        verifyRequest.setEmail("test@example.com");
        verifyRequest.setPassword("password");
        verifyRequest.setTwoFaCode("B-123456");

    }

    private UserEntity getUser() {
        return UserEntity.builder()
                .username("test")
                .password("encodedPassword")
                .email("test@example.com")
                .role(RoleEnum.CUSTOMER)
                .fullName("Test User")
                .twoFaCode("B-123456")
                .twoFaExpiry(ZonedDateTime.now().plusMinutes(10).toString())
                .build();
    }

    private AuthResponse getAuthResponse() {
        return AuthResponse.builder().authenticationStatus(AuthenticationStatus.SUCCESS).user(getUser()).build();
    }

    @Test
    public void testRegisterUser_Success() {

        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(getUser());
        when(tokenUtil.generateToken(any(UserEntity.class))).thenReturn("jwtToken");

        AuthResponse response = authenticationService.registerUser(authRequest);

        assertEquals(getAuthResponse().toString(), response.toString());
        verify(queueService, times(1)).sendTwoFactorAuthentication(any(UserEntity.class));
        verify(validatorUtil, times(1)).validate(any(UserEntity.class));
    }

    @Test
    public void testRegisterUser_UserAlreadyRegistered() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(new UserEntity()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authenticationService.registerUser(authRequest));

        assertEquals(ErrorEnums.USER_ALREADY_REGISTERED, exception.getErrorEnums());
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(queueService, never()).sendTwoFactorAuthentication(any(UserEntity.class));
    }

    @Test
    public void testAuthenticateUser_Success() {

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(getUser()));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(tokenUtil.generateToken(any(UserEntity.class))).thenReturn("jwtToken");

        AuthResponse response = authenticationService.authenticateUser(authRequest, false);

        assertEquals(getAuthResponse().toString(), response.toString());
        verify(queueService, times(1)).sendTwoFactorAuthentication(any(UserEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testAuthenticateUser_InvalidCredentials() {

        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () -> authenticationService.authenticateUser(authRequest, false));

        assertEquals(ErrorEnums.INVALID_CREDENTIALS, exception.getErrorEnums());
        verify(queueService, never()).sendTwoFactorAuthentication(any(UserEntity.class));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testVerifyUser_Success() {

        when(userRepository.save(any(UserEntity.class))).thenReturn(getUser());
        when(userRepository.findByUsername(getUser().getUsername())).thenReturn(Optional.of(getUser()));

        AuthResponse response = authenticationService.verifyUser(verifyRequest);

        assertEquals(getAuthResponse().toString(), response.toString());
        verify(queueService, times(1)).sendVerificationSuccessNotification(any(UserEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testVerifyUser_TwoFaAlreadyVerified() {

        UserEntity userEntity = getUser();
        userEntity.setVerified(true);

        when(userRepository.findByUsername(getUser().getUsername())).thenReturn(Optional.of(userEntity));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authenticationService.verifyUser(verifyRequest));

        assertEquals(ErrorEnums.TWO_FA_ALREADY_VERIFIED, exception.getErrorEnums());

        verify(queueService, never()).sendVerificationSuccessNotification(any(UserEntity.class));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testVerifyUser_Invalid2FaCode() {

        when(userRepository.findByUsername(getUser().getUsername())).thenReturn(Optional.of(getUser()));
        verifyRequest.setTwoFaCode("B-654321"); // Set an invalid 2FA code

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () -> authenticationService.verifyUser(verifyRequest));

        assertEquals(ErrorEnums.INVALID_2FA_CODE, exception.getErrorEnums());
        verify(queueService, never()).sendVerificationSuccessNotification(any(UserEntity.class));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

}
