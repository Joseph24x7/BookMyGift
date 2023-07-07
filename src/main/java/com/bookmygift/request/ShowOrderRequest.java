package com.bookmygift.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShowOrderRequest {

    private String giftType;
    private String orderStatus;
    @JsonProperty
    private transient String username;

}
