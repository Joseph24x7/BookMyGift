package com.bookmyticket.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bookmyticket.entity.TheatreInfo;

public interface TheatreManagementRepository extends MongoRepository<TheatreInfo, Integer>{

	@Query("{theatreCode : ?0}")
	TheatreInfo getTheatreInfoByCode(String theatreCode);
	
	@Query("{theatreCode : ?0}")
	Optional<TheatreInfo> getTheatreToDeleteMovie(String theatreCode);
	
}
