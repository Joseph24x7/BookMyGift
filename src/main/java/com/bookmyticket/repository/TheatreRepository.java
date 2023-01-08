package com.bookmyticket.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bookmyticket.entity.Theatre;

public interface TheatreRepository extends MongoRepository<Theatre, Integer>{

	@Query("{theatreCode : ?0}")
	Theatre getTheatreInfoByCode(String theatreCode);
	
	@Query("{theatreCode : ?0}")
	Optional<Theatre> getTheatreToDeleteMovie(String theatreCode);
	
}
