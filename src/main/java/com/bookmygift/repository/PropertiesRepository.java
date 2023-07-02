package com.bookmygift.repository;

import com.bookmygift.entity.Properties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {

    Optional<Properties> findByPropName(String orderId);

}
