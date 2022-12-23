package com.bookmyticket.entity;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookMyTicket {
	
	private Map<String, List<TheatreInfo>> recommendedMovies;

}
