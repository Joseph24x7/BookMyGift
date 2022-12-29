package com.bookmyticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookmyticketApplicationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void testGet() {
		ResponseEntity<String> response = testRestTemplate.getForEntity("/getAllRecommendedMovies",String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
