package com.bookmygift.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bookmygift.entity.Theatre;

public interface TheatreRepository extends MongoRepository<Theatre, Integer>{

	@Query("{theatreCode : ?0}")
	Optional<Theatre> getTheatreInfoByCode(String theatreCode);
	
}
