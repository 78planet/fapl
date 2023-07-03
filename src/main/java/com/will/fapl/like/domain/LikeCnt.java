package com.will.fapl.like.domain;

import static com.google.common.base.Preconditions.checkArgument;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeCnt {

    @Column(name = "like_cnt", nullable = false)
    private Long value;

    public LikeCnt(Long value) {
        validateLikeCnt(value);
        this.value = value;
    }

    private void validateLikeCnt(Long value) {
        checkArgument(0 <= value, "좋아요수는 양수만 가능합니다.");
    }

    public void plus() {
        this.value++;
    }

    public void minus() {
        if (this.value > 0) {
            this.value--;
        }
    }
}
