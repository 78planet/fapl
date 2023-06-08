package com.will.fapl.auth.presentation;

import com.will.fapl.auth.aop.Login;
import com.will.fapl.auth.aop.LoginRequired;
import com.will.fapl.auth.application.AuthFacade;
import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.application.dto.request.SignInRequest;
import com.will.fapl.auth.application.dto.response.LoginResponseDto;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.auth.presentation.dto.LoginResponse;
import com.will.fapl.common.model.ApiResponse;
import com.will.fapl.common.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "인증 관련 API입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @Operation(summary = "signin", description = "signin API")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<LoginResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authFacade.login(signInRequest);
        setTokenCookie(response, loginResponseDto.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(LoginResponse.from(loginResponseDto)));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API입니다.")
    @LoginRequired
    @PostMapping("/signout")
    public ResponseEntity<Void> signout(@Login LoginMember loginMember) {
        authFacade.logout(loginMember);
        return ResponseEntity.noContent().build();
    }

    private void setTokenCookie(HttpServletResponse response, TokenDto tokenDto) {
        ResponseCookie cookie = CookieUtil.createTokenCookie(tokenDto);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
