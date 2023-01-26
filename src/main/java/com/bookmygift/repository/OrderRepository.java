package com.bookmygift.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bookmygift.entity.Order;

public interface OrderRepository extends MongoRepository<Order, String>{

	@Query("{theatreCode : ?0}")
	Optional<Order> getTheatreInfoByCode(String theatreCode);
	
}
