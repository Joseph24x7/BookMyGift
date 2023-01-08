
package com.bookmyticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bookmyticket.info.AuthInfo;
import com.bookmyticket.info.AuthenticationResponse;
import com.bookmyticket.info.BookMyTicket;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookmyticketApplicationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void testGet() {

		AuthInfo authInfo = new AuthInfo();
		authInfo.setUsername("joseph");
		authInfo.setPassword("user@123");
		authInfo.setEmail("Joseph@gmail.com");

		HttpEntity<AuthInfo> requestEntity = new HttpEntity<>(authInfo);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(
				testRestTemplate.postForEntity("/api/v1/auth/authenticate", requestEntity, AuthenticationResponse.class)
						.getBody().getToken());

		ResponseEntity<BookMyTicket> response = testRestTemplate.exchange("/getAllRecommendedMovies", HttpMethod.GET,
				new HttpEntity<>(headers), BookMyTicket.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
