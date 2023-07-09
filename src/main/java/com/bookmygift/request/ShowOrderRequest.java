package com.bookmygift.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowOrderRequest {

    private String giftType;
    private String orderStatus;
    @JsonProperty
    private transient String username;

}
