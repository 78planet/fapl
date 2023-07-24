package com.will.fapl.point.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointTest {

    @DisplayName("포인트 생성 성공")
    @Test
    void create_success() {
        // given
        // when
        Point point = new Point(12L);

        // then
        assertThat(point).isNotNull();
    }
}
