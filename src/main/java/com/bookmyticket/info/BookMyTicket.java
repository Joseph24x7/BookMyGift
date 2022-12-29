package com.bookmyticket.info;

import java.util.List;

import com.bookmyticket.entity.Theatre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class BookMyTicket {
	
	private List<Theatre> recommendedTheatres;
	private Theatre theatre;
	
}