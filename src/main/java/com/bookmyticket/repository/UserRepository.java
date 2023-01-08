package com.bookmyticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmyticket.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
			
	Optional<User> findByUsername(String username);

}
