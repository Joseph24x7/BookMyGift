package com.bookmygift.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.ServiceException;
import com.bookmygift.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.util.StringUtils;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Qualifier("JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenGenerator jwtService;
	private final ObservationRegistry observationRegistry;
	private final UserDetailsService userDetailsService;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		try {
			
			final var authHeader = request.getHeader(CommonUtils.AUTHORIZATION);
			if (request.getRequestURI().contains("/api/v1/auth") || request.getRequestURI().contains("/swagger-ui/") || request.getRequestURI().contains("/v3/api-docs")) {
				
				filterChain.doFilter(request, response);
				return;
				
			}else if (StringUtils.isEmpty(authHeader)) {
				
				throw new ServiceException(ErrorEnums.TOKEN_REQUIRED);
				
			}

			final var jwt = authHeader.replace("Bearer ", "");
			final var username = jwtService.extractUsername(jwt);
			
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				var userDetails = this.userDetailsService.loadUserByUsername(username);
				
				if (jwtService.isTokenValid(jwt, userDetails)) {
					
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
					
				}
			}
			filterChain.doFilter(request, response);

		} catch (ServiceException e) {

			response.setStatus(e.getErrorEnums().getHttpStatus().value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(objectMapper.writeValueAsString(populateException(e.getErrorEnums().getHttpStatus(),e.getMessage(), e.getErrorEnums().getErrorCode(), request)));

		} catch (Exception e) {

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(objectMapper.writeValueAsString(populateException(ErrorEnums.UNAUTHORIZED.getHttpStatus(),e.getMessage(), ErrorEnums.UNAUTHORIZED.getErrorCode(), request)));
		}

	}

	private ProblemDetail populateException(HttpStatus httpStatus, String errorDescription, String errorCode,
			HttpServletRequest request) {

		var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorDescription);
		
		problemDetail.setTitle(errorCode);

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry).observe(() -> problemDetail);

	}
}
