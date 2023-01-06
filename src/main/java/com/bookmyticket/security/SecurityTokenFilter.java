/*
 * package com.bookmyticket.security;
 * 
 * import java.io.IOException;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.MediaType; import
 * org.springframework.http.ProblemDetail; import
 * org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.security.web.authentication.
 * WebAuthenticationDetailsSource; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * import com.bookmyticket.advice.ErrorEnums; import
 * com.fasterxml.jackson.databind.ObjectMapper;
 * 
 * import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException;
 * import jakarta.servlet.http.HttpServletRequest; import
 * jakarta.servlet.http.HttpServletResponse;
 * 
 * @Component public class SecurityTokenFilter extends OncePerRequestFilter {
 * 
 * @Autowired private CustomUserDetailsService customUserDetailsService;
 * 
 * @Autowired private TokenGenerator tokenGenerator;
 * 
 * @Override protected void doFilterInternal(HttpServletRequest request,
 * HttpServletResponse response, FilterChain filterChain) throws
 * ServletException, IOException {
 * 
 * try {
 * 
 * String authorizationHeader = request.getHeader("Authorization"); if
 * (authorizationHeader != null) {
 * 
 * String token = authorizationHeader.replace("Bearer ", ""); String userName =
 * tokenGenerator.extractUsername(token);
 * 
 * if (userName != null &&
 * SecurityContextHolder.getContext().getAuthentication() == null) {
 * 
 * UserDetails userDetails =
 * customUserDetailsService.loadUserByUsername(userName);
 * 
 * if (tokenGenerator.validateToken(token, userDetails)) {
 * 
 * UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
 * UsernamePasswordAuthenticationToken( userDetails, null,
 * userDetails.getAuthorities()); usernamePasswordAuthenticationToken
 * .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
 * SecurityContextHolder.getContext().setAuthentication(
 * usernamePasswordAuthenticationToken); } } }
 * 
 * filterChain.doFilter(request, response);
 * 
 * } catch (RuntimeException e) {
 * 
 * response.setStatus(HttpStatus.UNAUTHORIZED.value());
 * 
 * response.setContentType(MediaType.APPLICATION_JSON.toString());
 * 
 * ProblemDetail problemDetail =
 * ProblemDetail.forStatusAndDetail(ErrorEnums.UNAUTHORIZED.getHttpStatus(),
 * ErrorEnums.UNAUTHORIZED.getErrorDescription());
 * problemDetail.setTitle(ErrorEnums.UNAUTHORIZED.getErrorCode());
 * 
 * response.getWriter().write(new
 * ObjectMapper().writeValueAsString(problemDetail));
 * 
 * }
 * 
 * }
 * 
 * }
 */