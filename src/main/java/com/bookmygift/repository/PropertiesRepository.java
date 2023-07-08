package com.bookmygift.repository;

import com.bookmygift.entity.PropertiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertiesRepository extends JpaRepository<PropertiesEntity, Long> {

    Optional<PropertiesEntity> findByPropName(String propName);

}
