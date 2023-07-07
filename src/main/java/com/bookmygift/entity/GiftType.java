package com.bookmygift.entity;

import java.util.Optional;

public enum GiftType {

    KEYCHAIN,
    FRAME;

    public static Optional<GiftType> fromValue(String value) {
        try {
            return Optional.of(GiftType.valueOf(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
