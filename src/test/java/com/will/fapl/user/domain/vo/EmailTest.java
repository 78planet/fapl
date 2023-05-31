package com.will.fapl.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @DisplayName("이메일 생성 성공")
    @ParameterizedTest(name = "email: {0}")
    @ValueSource(strings = {"test@gmail.com", "test2@naver.com", "test333@naver.com"})
    void createEmail_success(String email) {
        // given
        // when
        Email email1 = new Email(email);

        // then
        assertThat(email).isNotNull();
    }

    @DisplayName("이메일 생성 실패 - 빈 값")
    @ParameterizedTest(name = "email: {0}")
    @NullAndEmptySource
    void createEmail_nullOrBlank_fail(String email) {

        assertThatThrownBy(() -> new Email(email))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이메일은 비어있을 수 없습니다.");
    }

    @DisplayName("이메일 생성 최대 글자수 초과로 인한 실패")
    @Test
    void createEmail_overMaxLength_fail() {
        // given
        String value = "test@gmail.com" + "a".repeat(37);

        // when
        // then
        assertThatThrownBy(() -> new Email(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("글자를 초과할 수 없습니다.");
    }

    @DisplayName("이메일 생성 이메일 형식 불일치로 인한 실패")
    @ParameterizedTest(name = "이메일 : {0}")
    @ValueSource(strings = {"test@nnnnn", "qqq", "test@.com"})
    void createEmail_invalidEmail_fail(String value) {
        assertThatThrownBy(() -> new Email(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("올바른 이메일 형식이 아닙니다.");
    }
}
