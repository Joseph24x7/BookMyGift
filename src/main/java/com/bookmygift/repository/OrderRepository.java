package com.bookmygift.repository;

import com.bookmygift.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query("{theatreCode : ?0}")
    Optional<Order> getTheatreInfoByCode(String theatreCode);

}
