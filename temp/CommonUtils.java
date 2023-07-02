package com.bookmygift.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;

public class CommonUtils {

    public static final String SERVICE_NAME = "ServiceName";

    private CommonUtils() {

    }

    public static ProblemDetail populateException(HttpStatus httpStatus, String errorDescription, String errorCode, ObservationRegistry observationRegistry, HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorDescription);
        problemDetail.setTitle(errorCode);

        return Observation.createNotStarted(request.getHeader(CommonUtils.SERVICE_NAME), observationRegistry)
                .observe(() -> problemDetail);

    }

}
