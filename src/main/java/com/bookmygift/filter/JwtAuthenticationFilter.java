package com.bookmygift.filter;

import com.bookmygift.exception.ErrorEnums;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Qualifier("JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {

        try {

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (request.getRequestURI().contains("/api/v1/auth")) {

                filterChain.doFilter(request, response);

            } else if (StringUtils.isNotBlank(authHeader)) {

                String username = tokenUtil.extractUsernameFromRequest(request);
                String jwt = authHeader.replace("Bearer ", "");

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    if (tokenUtil.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                    filterChain.doFilter(request, response);

                }
            } else {

                throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_REQUIRED);

            }

        } catch (UnAuthorizedException e) {
            populateResponse(response, e.getErrorEnums().getErrorCode(), e.getMessage());
        } catch (Exception e) {
            populateResponse(response, ErrorEnums.INVALID_CREDENTIALS.getErrorCode(), e.getMessage());
        }
    }

    private void populateResponse(HttpServletResponse response, String errorCode, String errorMessage) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, errorMessage);
        problemDetail.setTitle(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }

}
