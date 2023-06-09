package com.will.fapl.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.domain.BlackListRepository;
import com.will.fapl.util.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BlackListServiceTest extends IntegrationTest {

    @Autowired
    private BlackListService blackListService;

    @Autowired
    private BlackListRepository blackListRepository;

    @DisplayName("블랙 리스트 토큰 저장 성공")
    @Test
    void saveBlackList_success() {
        // given
        String accessToken = "accessToken";
        long expiredTime = 1000L;

        // when
        blackListService.saveBlackList(new TokenDto(accessToken, expiredTime));

        // then
        assertThat(blackListRepository.findById(accessToken)).isNotEmpty();
    }

    @DisplayName("블랙 리스트 확인 성공")
    @Test
    void isBlackList_success() {
        // given
        String accessToken = "accessToken";
        long expiredTime = 1000L;
        blackListService.saveBlackList(new TokenDto(accessToken, expiredTime));

        // when
        boolean result = blackListService.isBlackList(accessToken);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("블랙 리스트가 아님으로 인한 실패")
    @Test
    void isBlackList_notBlackList_fail() {
        // given
        String accessToken = "notBlackList";

        // when
        boolean result = blackListService.isBlackList(accessToken);

        // then
        assertThat(result).isFalse();
    }
}
