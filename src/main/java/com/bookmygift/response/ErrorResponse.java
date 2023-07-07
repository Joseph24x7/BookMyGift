package com.bookmygift.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String errorType;
    private String errorDescription;
    private String errorDetail;

}
