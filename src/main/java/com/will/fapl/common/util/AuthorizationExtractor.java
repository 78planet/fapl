package com.will.fapl.common.util;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.google.common.base.Strings;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (Strings.isNullOrEmpty(header)) {
            return null;
        }
        return extractToken(header);
    }

    private static String extractToken(String header) {
        if (header.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            String authHeaderValue = header.substring(BEARER_TYPE.length()).trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return authHeaderValue;
        }
        return null;
    }
}
