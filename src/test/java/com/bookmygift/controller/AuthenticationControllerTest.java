package com.bookmygift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bookmygift.security.info.AuthRequest;
import com.bookmygift.security.info.AuthResponse;
import com.bookmygift.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.micrometer.observation.Observation;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationService authenticationService;

	AuthRequest authRequest;

	@BeforeEach
	void populateRequest() {
		authRequest = AuthRequest.builder().username("username").password("password").email("user@email.com")
				.fullname("tommy").build();
	}

	private String getRequestBody(AuthRequest infoObject) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper.writeValueAsString(infoObject);
	}

	@Test
	void registerTest() throws Exception {

		// mock service method
		Mockito.when(authenticationService.register(authRequest)).thenReturn(new AuthResponse());

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("register"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		// perform post request
		mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(getRequestBody(authRequest))).andExpect(status().isOk());

	}
	
	@Test
	void authenticateTest() throws Exception {

		// mock service method
		Mockito.when(authenticationService.authenticate(authRequest)).thenReturn(new AuthResponse());

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("authenticate"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		// perform post request
		mockMvc.perform(post("/api/v1/auth/authenticate").contentType(MediaType.APPLICATION_JSON).content(getRequestBody(authRequest))).andExpect(status().isOk());

	}

}
