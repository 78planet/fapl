package com.will.fapl.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RefreshTokenTest {

    @Test
    public void refreshTokenCreate_success() {
        // given
        String id = "someId";
        String value = "someValue";
        long expiredTime = System.currentTimeMillis() + 1000; // Expire 1 second later

        // when
        RefreshToken refreshToken = new RefreshToken(id, value, expiredTime);

        // then
        assertThat(id).isEqualTo(refreshToken.getId());
        assertThat(value).isEqualTo(refreshToken.getValue());
        assertThat(expiredTime).isEqualTo(refreshToken.getExpiredTime());
    }

    @Test
    public void refreshTokenCreateIsSame_success() {
        String id = "someId";
        String value = "someValue";
        long expiredTime = System.currentTimeMillis() + 1000; // Expire 1 second later
        RefreshToken refreshToken = new RefreshToken(id, value, expiredTime);

        assertThat(refreshToken.isSame("someValue")).isTrue();
        assertThat(refreshToken.isSame("differentValue")).isFalse();
    }
}
