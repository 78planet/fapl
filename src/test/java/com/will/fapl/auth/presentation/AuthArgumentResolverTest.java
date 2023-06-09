package com.will.fapl.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.auth.infra.JwtTokenProvider;
import com.will.fapl.auth.presentation.resolver.AuthArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

public class AuthArgumentResolverTest {

    private static final JwtTokenProvider PROVIDER = new JwtTokenProvider(
        "test",
        "twk4jbz8a6smC4u0Xv6KvQUImMfVZ16/SCR0uKJIv3g=",
        60_000,
        60_000
    );

    private AuthArgumentResolver authArgumentResolver;

    @BeforeEach
    void setUp() {
        authArgumentResolver = new AuthArgumentResolver(PROVIDER);
    }

    @DisplayName("resolver 로 request 의 token 을 사용해서 LoginMember 객체 만들기 성공")
    @Test
    void resolveArgument_couple_success() {
        // given
        TokenDto token = PROVIDER.createAccessToken("1");
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getValue());
        NativeWebRequest request = new ServletWebRequest(servletRequest);

        // when
        Object result = authArgumentResolver.resolveArgument(null, null, request, null);

        // then
        assertAll(
            () -> assertThat(result).isInstanceOf(LoginMember.class)
        );
    }
}
