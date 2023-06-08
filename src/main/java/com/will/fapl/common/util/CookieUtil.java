package com.will.fapl.common.util;

import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.exception.UnauthorizedException;
import com.will.fapl.common.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public static ResponseCookie createTokenCookie(TokenDto tokenDto) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, tokenDto.getValue())
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(Duration.ofMillis(tokenDto.getExpiredTime()))
            .build();
    }

    public static String getTokenCookieValue(HttpServletRequest request) {
        Cookie findCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
            .findAny()
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN));
        return findCookie.getValue();
    }
}
