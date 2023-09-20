package com.bookmygift.response;

import com.bookmygift.entity.OrderEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class OrderResponse {

    private OrderEntity orderEntity;
    private List<OrderEntity> orderEntities;

}
