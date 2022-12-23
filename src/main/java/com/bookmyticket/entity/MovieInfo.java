package com.bookmyticket.entity;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfo {

	private String movieName;

	private Integer availableSeatCount;

	private Double ticketPrice;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MovieInfo)) {
			return false;
		}
		MovieInfo other = (MovieInfo) obj;
		return Objects.equals(movieName, other.movieName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(availableSeatCount, movieName, ticketPrice);
	}

	public MovieInfo(String movieName) {
		super();
		this.movieName = movieName;
	}


}
