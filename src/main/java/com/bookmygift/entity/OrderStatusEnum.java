package com.bookmygift.entity;

import java.util.Optional;

public enum OrderStatusEnum {

    ORDER_RECEIVED, CANCELLED;

    public static Optional<OrderStatusEnum> fromValue(String value) {
        try {
            return Optional.of(OrderStatusEnum.valueOf(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
