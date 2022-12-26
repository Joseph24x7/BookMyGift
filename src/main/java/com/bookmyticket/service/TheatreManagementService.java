package com.bookmyticket.service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.bookmyticket.advice.ErrorEnums;
import com.bookmyticket.advice.ServiceException;
import com.bookmyticket.entity.Theatre;
import com.bookmyticket.info.BookMyTicket;
import com.bookmyticket.repository.TheatreManagementRepository;

@Service
public class TheatreManagementService {

	@Autowired
	private TheatreManagementRepository theatreInfoRepository;

	public Theatre addMovieToTheatre(Theatre theatreInfo) {

		Theatre theatreInfoFromDb = theatreInfoRepository.getTheatreInfoByCode(theatreInfo.getTheatreCode());

		if (theatreInfoFromDb != null) {

			theatreInfoFromDb.getMovieDetails().addAll(theatreInfo.getMovieDetails());

			theatreInfoRepository.save(theatreInfoFromDb);

			return theatreInfoFromDb;

		} else {

			theatreInfo.setUniqueId(ObjectId.get());

			theatreInfoRepository.save(theatreInfo);

			return theatreInfo;

		}

	}

	public BookMyTicket getAllRecommendedMovies(String theatreName, Integer pincode) {

		Example<Theatre> example = Example.of(new Theatre(theatreName, pincode));

		List<Theatre> theatreInfos = theatreInfoRepository.findAll(example);

		Map<String, List<Theatre>> recommendedMovies = theatreInfos.stream()
				.flatMap(theatre -> theatre.getMovieDetails().stream()
						.map(movie -> new AbstractMap.SimpleEntry<>(theatre, movie)))
				.collect(Collectors.groupingBy(entry -> entry.getValue().getMovieName(),
						Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

		return BookMyTicket.builder().recommendedMovies(recommendedMovies).build();

	}

	public Theatre deleteMovieFromTheatre(Theatre theatreInfo) {

		Theatre theatreInfoFromDb = theatreInfoRepository.getTheatreToDeleteMovie(theatreInfo.getTheatreCode())
				.orElseThrow(() -> new ServiceException(ErrorEnums.THEATRE_CODE_INVALID));

		theatreInfoFromDb.getMovieDetails().removeAll(theatreInfo.getMovieDetails());

		theatreInfoRepository.save(theatreInfoFromDb);

		return theatreInfoFromDb;

	}

}
