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

import com.bookmyticket.entity.BookMyTicket;
import com.bookmyticket.entity.TheatreInfo;
import com.bookmyticket.service.TheatreManagementService;

@RestController
public class TheatreManagementController {

	@Autowired
	private TheatreManagementService theatreManagementService;

	@PostMapping("/addMovieToTheatre")
	public ResponseEntity<TheatreInfo> addMovieToTheatre(@RequestBody TheatreInfo theatreInfo) {
		return new ResponseEntity<>(theatreManagementService.addMovieToTheatre(theatreInfo), HttpStatus.CREATED);
	}

	@GetMapping("/getAllRecommendedMovies")
	public ResponseEntity<BookMyTicket> getAllRecommendedMovies(
			@RequestParam(value = "theatreName", required = false) String theatreName,
			@RequestParam(value = "pincode", required = false) Integer pincode) {
		return new ResponseEntity<>(theatreManagementService.getAllRecommendedMovies(theatreName,pincode), HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteMovieFromTheatre")
	public ResponseEntity<TheatreInfo> deleteMovieFromTheatre(@RequestBody TheatreInfo theatreInfo) {
		return new ResponseEntity<>(theatreManagementService.deleteMovieFromTheatre(theatreInfo), HttpStatus.CREATED);
	}
}
