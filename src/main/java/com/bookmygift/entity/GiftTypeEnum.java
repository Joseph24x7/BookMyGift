package com.bookmygift.entity;

import java.util.Optional;

public enum GiftTypeEnum {

    KEYCHAIN, FRAME;

    public static Optional<GiftTypeEnum> fromValue(String value) {
        try {
            return Optional.of(GiftTypeEnum.valueOf(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
