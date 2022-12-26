
package com.bookmyticket.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bookmyticket.info.BookMyTicket;
import com.bookmyticket.service.TheatreManagementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.micrometer.observation.Observation;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TheatreManagementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TheatreManagementService theatreManagementService;

	BookMyTicket infoObject = new BookMyTicket();

	public String getRequestBody(BookMyTicket infoObject) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper.writeValueAsString(infoObject);
	}

	@Test
	public void getAllRecommendedMovies() throws Exception {

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("getAllRecommendedMovies"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		mockMvc.perform(get("/getAllRecommendedMovies")).andExpect(status().isOk());

	}

	@Test
	public void addMovieToTheatre() throws Exception {

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("addMovieToTheatre"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		mockMvc.perform(
				post("/addMovieToTheatre").contentType(MediaType.APPLICATION_JSON).content(getRequestBody(infoObject)))
				.andExpect(status().isCreated());

	}

	@Test
	public void deleteMovieFromTheatre() throws Exception {

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("deleteMovieFromTheatre"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		mockMvc.perform(delete("/deleteMovieFromTheatre").contentType(MediaType.APPLICATION_JSON)
				.content(getRequestBody(infoObject))).andExpect(status().isAccepted());

	}
}
