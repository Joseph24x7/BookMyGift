package com.bookmygift.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmygift.info.User;

public interface UserRepository extends JpaRepository<User, Long>{
			
	Optional<User> findByUsername(String username);

}
