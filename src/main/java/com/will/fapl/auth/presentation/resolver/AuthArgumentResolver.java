package com.will.fapl.auth.presentation.resolver;

import com.google.common.base.Strings;
import com.will.fapl.auth.aop.Login;
import com.will.fapl.auth.application.JwtProvider;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.auth.exception.InvalidTokenException;
import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.common.util.AuthorizationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = AuthorizationExtractor.extract(httpServletRequest);
        if (Strings.isNullOrEmpty(accessToken)) {
            return new LoginMember();
        }

        LoginMember loginMember = getLoginMember(accessToken);
        return loginMember;
    }

    private LoginMember getLoginMember(String accessToken) {
        Long id = convertMemberId(accessToken);
        return new LoginMember(id, accessToken);
    }

    private Long convertMemberId(String token) {
        try {
            return Long.parseLong(jwtProvider.getSubject(token));
        } catch (NumberFormatException e) {
            throw new InvalidTokenException(token, ErrorCode.INVALID_TOKEN);
        }
    }
}
