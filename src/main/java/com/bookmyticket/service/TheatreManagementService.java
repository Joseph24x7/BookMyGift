package com.bookmyticket.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bookmyticket.entity.Theatre;
import com.bookmyticket.exception.ErrorEnums;
import com.bookmyticket.exception.ServiceException;
import com.bookmyticket.info.BookMyTicket;
import com.bookmyticket.repository.TheatreRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TheatreManagementService {

	private final TheatreRepository theatreInfoRepository;
	private final MongoTemplate mongoTemplate;

	public Theatre addMovieToTheatre(Theatre theatreInfo) {

		Optional<Theatre> theatreInfoFromDb = theatreInfoRepository.getTheatreInfoByCode(theatreInfo.getTheatreCode());

		if (theatreInfoFromDb.isPresent()) {

			Theatre theatre = theatreInfoFromDb.get();

			theatre.getMovieDetails().addAll(theatreInfo.getMovieDetails());

			theatreInfoRepository.save(theatre);

			return theatre;

		} else {

			theatreInfo.setUniqueId(ObjectId.get());

			theatreInfoRepository.save(theatreInfo);

			return theatreInfo;

		}

	}

	public BookMyTicket getAllRecommendedMovies(String theatreName, Integer pincode, String movieName) {
		
		Query query = new Query();

		Criteria criteria = new Criteria();

		if (StringUtils.isNotEmpty(movieName))
			criteria.and("movieDetails.movieName").is(movieName);

		if (pincode != null)
			criteria.and("pincode").is(pincode);

		if (StringUtils.isNotEmpty(theatreName))
			criteria.and("theatreName").is(theatreName);

		query.addCriteria(criteria);

		List<Theatre> theatreInfos = mongoTemplate.find(query, Theatre.class);

		return BookMyTicket.builder().recommendedTheatres(theatreInfos).build();

	}

	public Theatre deleteMovieFromTheatre(Theatre theatreInfo) {

		Theatre theatreInfoFromDb = theatreInfoRepository.getTheatreInfoByCode(theatreInfo.getTheatreCode())
				.orElseThrow(() -> new ServiceException(ErrorEnums.THEATRE_CODE_INVALID));

		theatreInfoFromDb.getMovieDetails().removeAll(theatreInfo.getMovieDetails());

		theatreInfoRepository.save(theatreInfoFromDb);

		return theatreInfoFromDb;

	}

}
