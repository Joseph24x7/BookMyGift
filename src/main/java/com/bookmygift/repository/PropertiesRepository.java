package com.bookmygift.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmygift.entity.Properties;

public interface PropertiesRepository extends JpaRepository<Properties, Long>{
	
	Optional<Properties> findByPropName(String propName);
	
}
