package com.bookmygift.reqresp;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShowOrderRequest {

    @NotBlank(message = "Please specify the type of gift.")
    @Pattern(regexp = "^(KEYCHAIN|FRAME)$", message = "Oops! It seems you've entered an invalid gift type. Only KEYCHAIN and FRAME are allowed.")
    private String giftType;

    @NotBlank(message = "Please select a valid order status.")
    @Pattern(regexp = "^(ORDER_RECEIVED|CANCELLED)$", message = "Oops! It seems you've selected an invalid order status. Only ORDER_RECEIVED and CANCELLED are allowed.")
    private String orderStatus;

    @JsonProperty
    private transient String username;

}
