package com.bookmyticket.entity;

import java.util.HashSet;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "TheatreInfo")
public class TheatreInfo {
	
	@Id
	private ObjectId uniqueId;
	
	@Indexed(unique=true)
	private String theatreCode;
	
	private String theatreName;
	
	private Integer pincode;
	
	private HashSet<MovieInfo> movieDetails;

	public TheatreInfo(String theatreName, Integer pincode) {
		super();
		this.theatreName = theatreName;
		this.pincode = pincode;
	}

	public TheatreInfo(String theatreName, Integer pincode, HashSet<MovieInfo> movieDetails) {
		super();
		this.theatreName = theatreName;
		this.pincode = pincode;
		this.movieDetails = movieDetails;
	}
	
}
