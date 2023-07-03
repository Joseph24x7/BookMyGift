package com.bookmygift.reqresp;

import com.bookmygift.entity.GiftType;
import com.bookmygift.entity.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull(message = "giftType should not be null")
    private GiftType giftType;

    @NotNull(message = "amountPaid should not be empty")
    @Min(value = 300, message = "amountPaid is less")
    private Double amountPaid;

    private OrderStatus orderStatus;
    private String orderId;
    private String username;

}
