package com.will.fapl.point.domain;

import static com.google.common.base.Preconditions.checkArgument;

import com.will.fapl.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "point")
    private User user;

    private Long point;

    public Point(Long point) {
        checkArgument(0 <= point, "point는 양수만 가능합니다.");
        this.point = point;
    }
}
