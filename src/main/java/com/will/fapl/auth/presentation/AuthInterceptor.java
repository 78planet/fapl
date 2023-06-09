package com.will.fapl.auth.presentation;

import com.google.common.base.Strings;
import com.will.fapl.auth.aop.LoginRequired;
import com.will.fapl.auth.application.BlackListService;
import com.will.fapl.auth.application.JwtProvider;
import com.will.fapl.auth.exception.InvalidTokenException;
import com.will.fapl.auth.exception.UnauthorizedException;
import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.common.util.AuthorizationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final BlackListService blackListService;

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response,
        Object handler) {
        if (CorsUtils.isPreFlightRequest(request) || isLoginRequired(handler) == false) {
            return true;
        }

        String accessToken = AuthorizationExtractor.extract(request);
        validateAccessToken(accessToken);
        validateBlackList(accessToken);
        return true;
    }

    private boolean isLoginRequired(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        LoginRequired auth = ((HandlerMethod)handler).getMethodAnnotation(LoginRequired.class);
        return !Objects.isNull(auth);
    }

    private void validateBlackList(String accessToken) {
        if (blackListService.isBlackList(accessToken)) {
            throw new UnauthorizedException(ErrorCode.BLACKLIST_TOKEN);
        }
    }

    private void validateAccessToken(String accessToken) {
        if (Strings.isNullOrEmpty(accessToken)) {
            throw new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN);
        }

        if (!jwtProvider.validateToken(accessToken)) {
            throw new InvalidTokenException(accessToken, ErrorCode.INVALID_TOKEN);
        }
    }
}
