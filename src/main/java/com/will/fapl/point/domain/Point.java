package com.will.fapl.point.domain;

import static com.google.common.base.Preconditions.checkArgument;

import com.will.fapl.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Column(name = "point", nullable = false)
    private Long value;

    public Point(Long value) {
        checkArgument(0 <= value, "point는 양수만 가능합니다.");
        this.value = value;
    }
}
