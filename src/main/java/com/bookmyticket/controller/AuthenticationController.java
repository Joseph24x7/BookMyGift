/*
 * package com.bookmyticket.controller;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.bookmyticket.advice.ErrorEnums; import
 * com.bookmyticket.advice.ServiceException; import
 * com.bookmyticket.security.AuthRequest; import
 * com.bookmyticket.security.TokenGenerator;
 * 
 * @RestController public class AuthenticationController {
 * 
 * @Autowired private AuthenticationManager authenticationManager;
 * 
 * @Autowired private TokenGenerator tokenGenerator;
 * 
 * @PostMapping("/authenticate") public String generateToken(@RequestBody
 * AuthRequest authRequest) throws Exception {
 * 
 * try { authenticationManager.authenticate(new
 * UsernamePasswordAuthenticationToken(authRequest.getUserName(),
 * authRequest.getPassword())); } catch (Exception ex) { throw new
 * ServiceException(ErrorEnums.UNAUTHORIZED); }
 * 
 * return tokenGenerator.generateToken(authRequest.getUserName()); }
 * 
 * }
 */