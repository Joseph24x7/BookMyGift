package com.bookmyticket.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class TheatreManagementService {

	@Autowired
	private TheatreRepository theatreInfoRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;

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

		Theatre theatreInfoFromDb = theatreInfoRepository.getTheatreToDeleteMovie(theatreInfo.getTheatreCode())
				.orElseThrow(() -> new ServiceException(ErrorEnums.THEATRE_CODE_INVALID));

		theatreInfoFromDb.getMovieDetails().removeAll(theatreInfo.getMovieDetails());

		theatreInfoRepository.save(theatreInfoFromDb);

		return theatreInfoFromDb;

	}

}
