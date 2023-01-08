package com.bookmyticket.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.bookmyticket.entity.Movie;
import com.bookmyticket.entity.Theatre;
import com.bookmyticket.info.BookMyTicket;
import com.bookmyticket.repository.TheatreRepository;

@SpringBootTest
class TheatreManagementServiceTest {
	
	@InjectMocks
	private TheatreManagementService theatreManagementService;
	
	@Mock
	private TheatreRepository theatreInfoRepository;
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	Theatre theatre = null;
	
	@BeforeEach
	public void init() {
		
		theatre = new Theatre();
		
		theatre.setPincode(600021);
		theatre.setTheatreCode("PVR");
		
		HashSet<Movie> movieDetails=new HashSet<>();
		movieDetails.add(new Movie());
		theatre.setMovieDetails(movieDetails);
	}
	
	@Test
	void addMovieToTheatreTest() {
		
		Mockito.when(theatreInfoRepository.getTheatreInfoByCode(Mockito.any())).thenReturn(theatre);
		
		Mockito.when(theatreInfoRepository.save(theatre)).thenReturn(theatre);
		
		Theatre theatreInfo=theatreManagementService.addMovieToTheatre(theatre);
		
		assertNotNull(theatreInfo);
		
	}
	
	@Test
	void addMovieToTheatreElseTest() {
		
		Mockito.when(theatreInfoRepository.getTheatreInfoByCode(Mockito.any())).thenReturn(null);
		
		Mockito.when(theatreInfoRepository.save(theatre)).thenReturn(theatre);
		
		Theatre theatreInfo=theatreManagementService.addMovieToTheatre(theatre);
		
		assertNotNull(theatreInfo.getUniqueId());
		
	}
	
	@Test
	void deleteMovieFromTheatreTest() {
		
		Optional<Theatre> optionalTheatre=Optional.of(theatre);
		
		Mockito.when(theatreInfoRepository.getTheatreToDeleteMovie(Mockito.any())).thenReturn(optionalTheatre);
		
		Mockito.when(theatreInfoRepository.save(optionalTheatre.get())).thenReturn(optionalTheatre.get());
		
		Theatre theatreInfo=theatreManagementService.deleteMovieFromTheatre(theatre);

		assertNotNull(theatreInfo);

	}
	
	@Test
	void getAllRecommendedMoviesTest() {
		
		List<Theatre> theatreInfos = Arrays.asList(theatre);
		
		Example<Theatre> example = Example.of(new Theatre(theatre.getTheatreCode(),theatre.getPincode()));
		
		Mockito.when(theatreInfoRepository.findAll(example)).thenReturn(theatreInfos);
		
		BookMyTicket bookMyTicket=theatreManagementService.getAllRecommendedMovies("PVR",600025,"Varisu");

		assertNotNull(bookMyTicket);

	}

}
