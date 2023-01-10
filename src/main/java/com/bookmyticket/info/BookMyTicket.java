package com.bookmyticket.info;

import java.util.List;

import com.bookmyticket.entity.Theatre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookMyTicket {
	
	private List<Theatre> recommendedTheatres;
	private Theatre theatre;
	
}
