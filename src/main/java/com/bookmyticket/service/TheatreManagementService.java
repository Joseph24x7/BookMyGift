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
import com.bookmyticket.entity.BookMyTicket;
import com.bookmyticket.entity.TheatreInfo;
import com.bookmyticket.repository.TheatreManagementRepository;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

@Service
public class TheatreManagementService {

	@Autowired
	private TheatreManagementRepository theatreInfoRepository;

	@Autowired
	private ObservationRegistry observationRegistry;

	public TheatreInfo addMovieToTheatre(TheatreInfo theatreInfo) {

		TheatreInfo theatreInfoFromDb = theatreInfoRepository.getTheatreInfoByCode(theatreInfo.getTheatreCode());

		if (theatreInfoFromDb != null) {

			theatreInfoFromDb.getMovieDetails().addAll(theatreInfo.getMovieDetails());

			theatreInfoRepository.save(theatreInfoFromDb);

			return Observation.createNotStarted("addMovieToTheatre", observationRegistry)
					.observe(() -> theatreInfoFromDb);

		} else {

			theatreInfo.setUniqueId(ObjectId.get());

			theatreInfoRepository.save(theatreInfo);

			return Observation.createNotStarted("addMovieToTheatre", observationRegistry)
					.observe(() -> theatreInfo);
			
		}

	}

	public BookMyTicket getAllRecommendedMovies(String theatreName, Integer pincode) {

		Example<TheatreInfo> example = Example.of(new TheatreInfo(theatreName, pincode));

		List<TheatreInfo> theatreInfos = theatreInfoRepository.findAll(example);

		Map<String, List<TheatreInfo>> recommendedMovies = theatreInfos.stream()
				.flatMap(theatre -> theatre.getMovieDetails().stream()
						.map(movie -> new AbstractMap.SimpleEntry<>(theatre, movie)))
				.collect(Collectors.groupingBy(entry -> entry.getValue().getMovieName(),
						Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

		return Observation.createNotStarted("getAllRecommendedMovies", observationRegistry)
				.observe(() -> BookMyTicket.builder().recommendedMovies(recommendedMovies).build());

	}

	public TheatreInfo deleteMovieFromTheatre(TheatreInfo theatreInfo) {

		TheatreInfo theatreInfoFromDb = theatreInfoRepository.getTheatreToDeleteMovie(theatreInfo.getTheatreCode())
				.orElseThrow(() -> new ServiceException(ErrorEnums.THEATRE_CODE_INVALID));

		theatreInfoFromDb.getMovieDetails().removeAll(theatreInfo.getMovieDetails());

		theatreInfoRepository.save(theatreInfoFromDb);

		return Observation.createNotStarted("deleteMovieFromTheatre", observationRegistry)
				.observe(() -> theatreInfoFromDb);

	}

}
