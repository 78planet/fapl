package com.will.fapl.member.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NickNameTest {

    @DisplayName("닉네임 생성 성공")
    @ParameterizedTest(name = "닉네임 : {0}")
    @ValueSource(strings = {"zear", "babo", "jason", "willllllliam"})
    void createNickName_success(String value) {
        // given
        // when
        NickName nickName = new NickName(value);

        // then
        assertThat(nickName).isNotNull();
    }

    @DisplayName("한글 닉네임 생성 실패")
    @ParameterizedTest(name = "닉네임 : {0}")
    @ValueSource(strings = {"영철", "옥순", "철수", "바보"})
    void createNickName_kor_fail(String value) {
        assertThatThrownBy(() -> new NickName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("닉네임은 영어와 숫자로 작성해주세요.");
    }

    @DisplayName("닉네임 생성 빈 값으로 인한 실패")
    @ParameterizedTest(name = "닉네임 : {0}")
    @NullAndEmptySource
    void createNickName_empty_fail(String value) {
        assertThatThrownBy(() -> new NickName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("닉네임은 비어있을 수 없습니다.");
    }

    @DisplayName("닉네임 생성 글자 수 초과로 인한 실패")
    @Test
    void createNickName_overOrLessLength_fail() {
        // given
        String value = "willbaboqqqooooo";

        // when
        // then
        assertThatThrownBy(() -> new NickName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("글자를 초과할 수 없습니다.");
    }
}
