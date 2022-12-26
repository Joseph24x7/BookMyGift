package com.bookmyticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookmyticket.entity.Theatre;
import com.bookmyticket.info.BookMyTicket;
import com.bookmyticket.service.TheatreManagementService;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TheatreManagementController {

	@Autowired
	private TheatreManagementService theatreManagementService;
	
	@Autowired
	private ObservationRegistry observationRegistry;

	@PostMapping("/addMovieToTheatre")
	public ResponseEntity<Theatre> addMovieToTheatre(@RequestBody BookMyTicket bookMyTicket,
			HttpServletRequest request) {

		return Observation.createNotStarted(request.getRequestURI().substring(1),observationRegistry).observe(
				() -> new ResponseEntity<>(theatreManagementService.addMovieToTheatre(bookMyTicket.getTheatre()),
						HttpStatus.CREATED));

	}

	@GetMapping("/getAllRecommendedMovies")
	public ResponseEntity<BookMyTicket> getAllRecommendedMovies(
			@RequestParam(value = "theatreName", required = false) String theatreName,
			@RequestParam(value = "pincode", required = false) Integer pincode, HttpServletRequest request) {

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry).observe(
				() -> new ResponseEntity<>(theatreManagementService.getAllRecommendedMovies(theatreName, pincode),
						HttpStatus.OK));

	}

	@DeleteMapping("/deleteMovieFromTheatre")
	public ResponseEntity<Theatre> deleteMovieFromTheatre(@RequestBody BookMyTicket bookMyTicket,
			HttpServletRequest request) {

		return Observation.createNotStarted(request.getRequestURI().substring(1), observationRegistry)
				.observe(() -> new ResponseEntity<>(
						theatreManagementService.deleteMovieFromTheatre(bookMyTicket.getTheatre()),
						HttpStatus.ACCEPTED));
	}
}
