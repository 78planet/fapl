package com.will.fapl.common.config;

import com.will.fapl.auth.application.BlackListService;
import com.will.fapl.auth.application.JwtProvider;
import com.will.fapl.auth.presentation.AuthInterceptor;
import com.will.fapl.auth.presentation.resolver.AuthArgumentResolver;
import com.will.fapl.auth.presentation.resolver.JwtTokenArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    private final JwtProvider jwtProvider;
    private final BlackListService blackListService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
            .allowCredentials(true)
            .exposedHeaders(HttpHeaders.LOCATION)
            .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtProvider, blackListService));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(jwtProvider));
        resolvers.add(new JwtTokenArgumentResolver());
    }
}
