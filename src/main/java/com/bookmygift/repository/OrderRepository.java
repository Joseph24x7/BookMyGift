package com.bookmygift.repository;

import com.bookmygift.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderIdAndUsername(String orderId, String username);

}
