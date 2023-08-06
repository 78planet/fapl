package com.will.fapl.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlackListTest {

    @DisplayName("BlackListTest 생성 성공")
    @Test
    public void blackListTestCreate_success() {
        // given
        String accessToken = "someAccessToken";
        long expiredTime = System.currentTimeMillis() + 1000; // Expire 1 second later

        // when
        BlackList blackList = new BlackList(accessToken, expiredTime);

        // then
        assertThat(accessToken).isEqualTo(blackList.getAccessToken());
        assertThat(expiredTime).isEqualTo(blackList.getExpiredTime());
    }
}
