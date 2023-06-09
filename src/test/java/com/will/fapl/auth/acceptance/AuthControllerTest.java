package com.will.fapl.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.will.fapl.auth.application.dto.request.SignInRequest;
import com.will.fapl.auth.application.dto.response.AccessTokenResponse;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.auth.presentation.dto.LoginResponse;
import com.will.fapl.common.exception.ErrorResponse;
import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.util.AcceptanceTest;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

public class AuthControllerTest extends AcceptanceTest {

    @BeforeEach
    void setUp() throws Exception {
        회원가입(new SignupRequest("fapl@gmail.com", "!Test11123", "will"));
    }

    @DisplayName("로그인 성공")
    @Test
    void signIn_success() throws Exception {
        // given
        String email = "fapl@gmail.com";
        String password = "!Test11123";
        SignInRequest signInRequest = new SignInRequest(email, password);

        // when
        MockHttpServletResponse response = 로그인(signInRequest);

        // then
        LoginResponse loginResponse = getResponseObject(response, LoginResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.getCookie("refreshToken")).isNotNull(),
            () -> assertThat(loginResponse.getAccessToken()).isNotBlank(),
            () -> assertThat(loginResponse.getMember().getEmail()).isEqualTo("fapl@gmail.com"),
            () -> assertThat(loginResponse.getMember().getNickName()).isEqualTo("will")
        );
    }

    @DisplayName("저장되지 않은 이메일로 인한 로그인 실패")
    @Test
    void signIn_notFoundEmail_fail() throws Exception {
        // given
        String email = "invalid@gmail.com";
        String password = "!Test11123";

        // when
        MockHttpServletResponse response = 로그인(new SignInRequest(email, password));

        // then
        로그인_실패(response);
    }

    @DisplayName("비밀번호 불일치로 인한 로그인 실패")
    @Test
    void signIn_invalidPassword_fail() throws Exception {
        // given
        String email = "fapl@gmail.com";
        String password = "invalidPassword";

        // when
        MockHttpServletResponse response = 로그인(new SignInRequest(email, password));

        // then
        로그인_실패(response);
    }

    @DisplayName("로그아웃 성공")
    @Test
    void logout_success() throws Exception {
        // given
        String email = "fapl@gmail.com";
        String password = "!Test11123";
        String accessToken = 로그인_토큰(new SignInRequest(email, password));

        // when
        MockHttpServletResponse response = 로그아웃(accessToken);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("로그아웃 이후 해당 토큰 사용 불가 성공")
    @Test
    void logout_blackList_success() throws Exception {
        // given
        String accessToken = 로그인_토큰(new SignInRequest("fapl@gmail.com", "!Test11123"));
        로그아웃(accessToken);

        // when
        MockHttpServletResponse response = 로그아웃(accessToken);

        // then
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
            () -> assertThat(errorResponse.getCode()).isEqualTo("A003"),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("허용되지 않는 토큰입니다.")
        );
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void refreshAccessToken_success() throws Exception {
        // given
        MockHttpServletResponse loginResponse = 로그인(new SignInRequest("fapl@gmail.com", "!Test11123"));
        String accessToken = getAccessToken(loginResponse);
        String refreshToken = getRefreshToken(loginResponse);

        // when
        MockHttpServletResponse response = 토큰_재발급_요청(accessToken, refreshToken);

        // then
        AccessTokenResponse accessTokenResponse = getResponseObject(response, AccessTokenResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(accessTokenResponse.getAccessToken()).isNotNull()
        );
    }

    @DisplayName("유효하지 않은 accessToken으로 인한 토큰 재발급 실패")
    @Test
    void refreshAccessToken_invalidAccessToken_fail() throws Exception {
        // given
        MockHttpServletResponse loginResponse = 로그인(new SignInRequest("fapl@gmail.com", "!Test11123"));
        String accessToken = getAccessToken(loginResponse);
        String refreshToken = getRefreshToken(loginResponse);

        // when
        MockHttpServletResponse response = 토큰_재발급_요청(accessToken + "invalid", refreshToken);

        // then
        토큰_재발급_실패(response);
    }

    @DisplayName("유효하지 않은 refreshToken으로 인한 토큰 재발급 실패")
    @Test
    void refreshAccessToken_invalidRefreshToken_fail() throws Exception {
        MockHttpServletResponse loginResponse = 로그인(new SignInRequest("fapl@gmail.com", "!Test11123"));
        String accessToken = getAccessToken(loginResponse);
        String refreshToken = getRefreshToken(loginResponse);

        // when
        MockHttpServletResponse response = 토큰_재발급_요청(accessToken, refreshToken + "invalid");

        // then
        토큰_재발급_실패(response);
    }


    @DisplayName("LoginMember 객체 반환 성공")
    @Test
    void getLoginMember_success() throws Exception {
        // given
        String accessToken = 로그인_토큰(new SignInRequest("fapl@gmail.com", "!Test11123"));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/auth/login/members")
                .content(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andDo(print())
            .andReturn().getResponse();

        // then
        LoginMember loginMember = getResponseObject(response, LoginMember.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(loginMember.getId()).isPositive()
        );
    }

    private MockHttpServletResponse 토큰_재발급_요청(String accessToken, String refreshToken) throws Exception {
        return mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .cookie(new Cookie("refreshToken", refreshToken)))
            .andReturn().getResponse();
    }

    private void 토큰_재발급_실패(MockHttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("유효하지 않은 토큰입니다.")
        );
    }

    private String getRefreshToken(MockHttpServletResponse response) {
        return response.getCookie("refreshToken").getValue();
    }

    private String getAccessToken(MockHttpServletResponse loginResponse) throws IOException {
        return getResponseObject(loginResponse, LoginResponse.class).getAccessToken();
    }

    private MockHttpServletResponse 로그아웃(String accessToken) throws Exception {
        return mockMvc.perform(post("/api/auth/signout")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andReturn().getResponse();
    }

    private void 로그인_실패(MockHttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("이메일 또는 비밀번호가 일치하지 않습니다."),
            () -> assertThat(errorResponse.getCode()).isEqualTo("M001")
        );
    }
}
