package com.will.fapl.auth.presentation.resolver;

import com.google.common.base.Strings;
import com.will.fapl.auth.exception.UnauthorizedException;
import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.common.util.AuthorizationExtractor;
import com.will.fapl.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver  {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestTokens.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = getAccessTokenByRequest(request);
        String refreshToken = CookieUtil.getTokenCookieValue(request);
        return new JwtToken(accessToken, refreshToken);
    }

    private String getAccessTokenByRequest(HttpServletRequest httpServletRequest) {
        String token = AuthorizationExtractor.extract(httpServletRequest);
        if (Strings.isNullOrEmpty(token)) {
            throw new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN);
        }
        return token;
    }
}
