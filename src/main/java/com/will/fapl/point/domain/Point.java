package com.will.fapl.point.domain;

import static com.google.common.base.Preconditions.checkArgument;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    private final Long LIKE_POINT = 50L;
    private final Long DISLIKE_POINT = 10L;

    @Column(name = "point", nullable = false)
    private Long value;

    public Point(Long value) {
        this.value = value;
    }

    public void addLikePoint() {
        this.value = this.value + LIKE_POINT;
    }

    public void cancelLikePoint() {
        this.value = this.value - LIKE_POINT;
    }

    public void addDislikePoint() {
        this.value = this.value - DISLIKE_POINT;
    }

    public void cancelDislikePoint() {
        this.value = this.value + DISLIKE_POINT;
    }
}
