/*
 * package com.bookmyticket.security;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.authentication.builders.
 * AuthenticationManagerBuilder; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class SecurityConfig {
 * 
 * @Autowired private CustomUserDetailsService customUserDetailsService;
 * 
 * @Autowired public void configAuthentication(AuthenticationManagerBuilder
 * auth) throws Exception { auth.userDetailsService(customUserDetailsService); }
 * 
 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
 * Exception {
 * 
 * http.csrf().disable().authorizeHttpRequests()
 * .requestMatchers("/authenticate", "/actuator/**", "/swagger-ui/**",
 * "/v3/**").permitAll().anyRequest()
 * .authenticated().and().exceptionHandling().and().sessionManagement()
 * .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
 * 
 * http.addFilterBefore(new SecurityTokenFilter(),
 * UsernamePasswordAuthenticationFilter.class);
 * 
 * return http.build(); }
 * 
 * }
 */