package com.bookmyticket.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmyticket.BookmyticketApplication;

@RestController
public class GetDetailsController {
	
	@GetMapping("/getAllRecommendedMovies")
	public ResponseEntity<List<String>> getAllRecommendedMovies() {
		return new ResponseEntity<>(BookmyticketApplication.recommendedMovies, HttpStatus.OK);
	}

}
