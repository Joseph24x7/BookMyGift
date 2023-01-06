package com.bookmyticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmyticket.info.User;

public interface UserRepository extends JpaRepository<User, Long>{
			
	User findByUserName(String username);

}
