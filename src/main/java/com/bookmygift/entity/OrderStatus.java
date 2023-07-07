package com.bookmygift.entity;

import java.util.Optional;

public enum OrderStatus {

    ORDER_RECEIVED, CANCELLED;

    public static Optional<OrderStatus> fromValue(String value) {
        try {
            return Optional.of(OrderStatus.valueOf(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
