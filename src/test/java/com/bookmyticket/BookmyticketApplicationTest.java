package com.bookmyticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bookmyticket.info.AuthInfo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookmyticketApplicationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	void testGet() {
		
		AuthInfo authInfo=new AuthInfo();
		authInfo.setUserName("hod");
		authInfo.setPassword("pass");
		
		HttpEntity<AuthInfo> requestEntity = new HttpEntity<>(authInfo);
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", testRestTemplate.postForEntity("/authenticate",requestEntity, String.class).getBody());
		
		ResponseEntity<String> response = testRestTemplate.getForEntity("/getAllRecommendedMovies",String.class,headers);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
