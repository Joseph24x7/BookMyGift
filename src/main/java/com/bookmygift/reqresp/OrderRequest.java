package com.bookmygift.reqresp;

import com.bookmygift.entity.GiftType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "giftType should not be null")
    private GiftType giftType;

    @NotNull(message = "amountPaid should not be empty")
    @Min(value = 300, message = "amountPaid is less")
    private Double amountPaid;
}
