
package com.bookmygift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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

import com.bookmygift.entity.Order;
import com.bookmygift.info.GiftType;
import com.bookmygift.info.OrderStatus;
import com.bookmygift.request.OrderRequest;
import com.bookmygift.service.BookMyGiftService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.micrometer.observation.Observation;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookMyGiftControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookMyGiftService bookMyGiftService;

	OrderRequest orderRequest;
	Order order;

	@BeforeEach
	void populateRequest() {

		order = new Order();
		orderRequest = OrderRequest.builder().giftType(GiftType.KEYCHAIN).amountPaid(300.0D).build();

	}

	private String getRequestBody(OrderRequest infoObject) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper.writeValueAsString(infoObject);
	}

	@Test
	void placeOrderTest() throws Exception {

		// mock service method
		Mockito.when(bookMyGiftService.placeOrder(orderRequest)).thenReturn(order);

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("placeOrder"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		// perform post request
		mockMvc.perform(
				post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(getRequestBody(orderRequest)))
				.andExpect(status().isCreated());

	}

	@Test
	void showMyOrdersTest() throws Exception {

		// mock service method
		Mockito.when(
				bookMyGiftService.showMyOrders(Mockito.any(GiftType.class), Mockito.any(OrderStatus.class)))
				.thenReturn(Arrays.asList(new Order(), new Order()));

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("showMyOrders"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		// perform get request
		mockMvc.perform(get("/showMyOrders").param("giftType", GiftType.KEYCHAIN.toString()).param("orderStatus",
				OrderStatus.ORDER_RECIEVED.toString())).andExpect(status().isOk());

	}

	@Test
	void cancelOrderTest() throws Exception {

		// mock service method
		Mockito.when(bookMyGiftService.cancelOrder(Mockito.anyString())).thenReturn(new Order());

		try (MockedStatic<Observation> utilities = Mockito.mockStatic(Observation.class)) {
			utilities.when(() -> Observation.createNotStarted(Mockito.eq("cancelOrder"), Mockito.any()))
					.thenReturn(Observation.NOOP);

		}

		// perform get request
		mockMvc.perform(delete("/cancelOrder").param("orderId", "orderId")).andExpect(status().isAccepted());

	}

}
