package com.bookmygift.reqresp;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotBlank(message = "Please specify the type of gift.")
    @Pattern(regexp = "^(KEYCHAIN|FRAME)$", message = "Oops! It seems you've entered an invalid gift type. Only KEYCHAIN and FRAME are allowed.")
    private String giftType;

    private Double amountPaid;

    @JsonProperty
    private transient String username;

}
