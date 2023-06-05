package com.will.fapl.user.domain;

import lombok.Getter;

@Getter
public enum Grade {

    BRONZE(1), SILVER(2), GOLD(3);

    private final Integer code;

    Grade(Integer code) {
        this.code = code;
    }
}
