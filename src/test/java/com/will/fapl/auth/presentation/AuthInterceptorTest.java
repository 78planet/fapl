package com.will.fapl.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.will.fapl.auth.aop.LoginRequired;
import com.will.fapl.auth.application.BlackListService;
import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.exception.InvalidTokenException;
import com.will.fapl.auth.exception.UnauthorizedException;
import com.will.fapl.auth.infra.JwtTokenProvider;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

@ExtendWith(MockitoExtension.class)
public class AuthInterceptorTest {

    private static final JwtTokenProvider PROVIDER = new JwtTokenProvider(
        "test",
        "twk4jbz8a6smC4u0Xv6KvQUImMfVZ16/SCR0uKJIv3g=",
        60_000,
        60_000
    );

    private AuthInterceptor authInterceptor;
    private BlackListService blackListService;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        blackListService = mock(BlackListService.class);
        authInterceptor = new AuthInterceptor(PROVIDER, blackListService);
        response = new MockHttpServletResponse();
    }

    @DisplayName("인터셉터 통과 성공")
    @Test
    void preHandle_success() {
        // given
        TokenDto token = PROVIDER.createAccessToken("1");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getValue());

        // when
        boolean result = authInterceptor.preHandle(request, new MockHttpServletResponse(), null);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("인증이 필요없는 요청일 경우 인터셉터 통과 성공")
    @Test
    void preHandle_notLoginRequired_success() throws NoSuchMethodException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        HandlerMethod handlerMethod = getNotLoginRequiredHandlerMethod();

        // when
        boolean result = authInterceptor.preHandle(request, new MockHttpServletResponse(), handlerMethod);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("헤더에 토큰이 없음으로 인한 실패")
    @Test
    void preHandle_notFoundToken_fail() throws NoSuchMethodException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        HandlerMethod handlerMethod = getLoginRequiredHandlerMethod();

        // when
        // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, handlerMethod))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage("로그인이 필요합니다.");
    }

    @DisplayName("유효하지 않은 토큰으로 인한 실패")
    @Test
    void preHandle_invalidToken_fail() throws NoSuchMethodException {
        // given
        String token = "invalidToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HandlerMethod handlerMethod = getLoginRequiredHandlerMethod();

        // when
        // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, handlerMethod))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("유효하지 않은 토큰입니다.");
    }

    @DisplayName("블랙 리스트에 등록된 토큰으로 인한 실패")
    @Test
    void preHandle_blackList_fail() throws NoSuchMethodException {
        // given
        TokenDto token = PROVIDER.createAccessToken("1");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getValue());
        HandlerMethod handlerMethod = getLoginRequiredHandlerMethod();
        given(blackListService.isBlackList(anyString())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, handlerMethod))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessageContaining("허용되지 않는 토큰입니다.");
    }

    private HandlerMethod getLoginRequiredHandlerMethod() throws NoSuchMethodException {
        Method method = MockController.class.getMethod("loginRequired");
        MockController mockController = new MockController();
        return new HandlerMethod(mockController, method);
    }

    private HandlerMethod getNotLoginRequiredHandlerMethod() throws NoSuchMethodException {
        Method method = MockController.class.getMethod("notLoginRequired");
        MockController mockController = new MockController();
        return new HandlerMethod(mockController, method);
    }

    static class MockController {

        @LoginRequired
        public void loginRequired() {
        }

        public void notLoginRequired() {
        }
    }
}
