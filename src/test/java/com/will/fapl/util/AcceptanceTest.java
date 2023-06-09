package com.will.fapl.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.will.fapl.auth.application.dto.request.SignInRequest;
import com.will.fapl.auth.presentation.dto.LoginResponse;
import com.will.fapl.common.exception.ErrorResponse;
import com.will.fapl.common.model.ApiResponse;
import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }

    protected MockHttpServletResponse 회원가입(SignupRequest signupRequest) throws Exception {
        return mockMvc.perform(post("/api/members/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected String 회원가입_토큰(SignupRequest signupRequest) throws Exception {
        회원가입(signupRequest);
        return 로그인_토큰(new SignInRequest(signupRequest.getEmail(), signupRequest.getPassword()));
    }

    protected MockHttpServletResponse 로그인(SignInRequest signInRequest) throws Exception {
        return mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signInRequest)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected String 로그인_토큰(SignInRequest signInRequest) throws Exception {
        MockHttpServletResponse response = 로그인(signInRequest);
        return "Bearer" + getResponseObject(response, LoginResponse.class).getAccessToken();
    }

    protected MockHttpServletResponse 게시글_작성(String token, CreatePostRequest request) throws Exception {
        return mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected MockHttpServletResponse 게시글_수정(String token, EditPostRequest request, Long postId) throws Exception {
        return mockMvc.perform(put("/api/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected MockHttpServletResponse 게시글_삭제(String token, Long postId) throws Exception {
        return mockMvc.perform(delete("/api/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andReturn().getResponse();
    }


    protected MockHttpServletResponse 게시글_좋아요(String token, Long postId) throws Exception {
        return mockMvc.perform(post("/api/posts/like/" + postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected MockHttpServletResponse 게시글_싫어요(String token, Long postId) throws Exception {
        return mockMvc.perform(post("/api/posts/dislike/" + postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andReturn().getResponse();
    }

    protected ErrorResponse getErrorResponse(MockHttpServletResponse response) throws IOException {
        return objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
    }

    protected <T> T getResponseObject(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, type);
        ApiResponse<T> result = objectMapper.readValue(response.getContentAsString(), javaType);
        return result.getData();
    }

    protected <T> List<T> getResponseList(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType insideType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, insideType);
        ApiResponse<List<T>> result = objectMapper.readValue(response.getContentAsString(), javaType);
        return result.getData();
    }
}
