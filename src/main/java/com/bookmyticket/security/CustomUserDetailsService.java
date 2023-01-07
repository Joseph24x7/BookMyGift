/*
 * package com.bookmyticket.security;
 * 
 * import java.util.ArrayList;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.security.core.userdetails.UserDetailsService; import
 * org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.stereotype.Service;
 * 
 * import com.bookmyticket.info.User; import
 * com.bookmyticket.repository.UserRepository;
 * 
 * @Service public class CustomUserDetailsService implements UserDetailsService
 * {
 * 
 * @Autowired private UserRepository userRepository;
 * 
 * @Autowired private PasswordEncoder passwordEncoder;
 * 
 * @Override public UserDetails loadUserByUsername(String username) throws
 * UsernameNotFoundException { User user =
 * userRepository.findByUserName(username); return new
 * org.springframework.security.core.userdetails.User(user.getUserName(),
 * passwordEncoder.encode(user.getPassword()), new ArrayList<>()); }
 * 
 * }
 */