package com.bookmygift.entity;

import java.util.Objects;

import lombok.Data;

@Data
public class Movie {

	private Integer availableSeatCount;

	private String movieName;

	private Double ticketPrice;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Movie)) {
			return false;
		}
		Movie other = (Movie) obj;
		return Objects.equals(movieName, other.movieName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieName);
	}

}
