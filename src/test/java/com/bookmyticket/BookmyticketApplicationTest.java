
package com.bookmyticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bookmyticket.info.BookMyTicket;
import com.bookmyticket.security.info.AuthRequest;
import com.bookmyticket.security.info.AuthResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookmyticketApplicationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void testGetAllRecommendedMovies() {

		AuthRequest authInfo = new AuthRequest();
		authInfo.setUsername("test");
		authInfo.setPassword("test@123");
		authInfo.setEmail("test@gmail.com");

		HttpEntity<AuthRequest> requestEntity = new HttpEntity<>(authInfo);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(
				testRestTemplate.postForEntity("/api/v1/auth/authenticate", requestEntity, AuthResponse.class)
						.getBody().getToken());

		ResponseEntity<BookMyTicket> response = testRestTemplate.exchange("/getAllRecommendedMovies", HttpMethod.GET,
				new HttpEntity<>(headers), BookMyTicket.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
}
