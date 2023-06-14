package com.will.fapl.post.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.application.dto.response.PostResponse;
import com.will.fapl.util.AcceptanceTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

class PostControllerTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void login() throws Exception {
        String email = "test@gmail.com";
        String password = "!Test1234";
        String nickName = "test";
        accessToken = 회원가입_토큰(new SignupRequest(email, password, nickName));
    }

    @DisplayName("post 생성 완료")
    @Test
    void create_post_success() throws Exception {
        // given
        CreatePostRequest createRequest = createPostRequest();

        // when
        MockHttpServletResponse response = 게시글_작성(accessToken, createRequest);

        // then
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.getHeader(HttpHeaders.LOCATION)).isNotNull()
        );
    }

    @DisplayName("게시물 단건 조회 성공")
    @Test
    void showPost_success() throws Exception {
        // given
        CreatePostRequest createRequest = createPostRequest();
        MockHttpServletResponse createPostResponse = 게시글_작성(accessToken, createRequest);
        Long postId = getPostId(createPostResponse);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/posts/{postId}", postId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andReturn().getResponse();

        // then
        PostResponse postResponse = getResponseObject(response, PostResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(postResponse.getId()).isPositive()
        );
    }


    @DisplayName("post 수정 완료")
    @Test
    void modify_post_success() throws Exception {
        // given
        CreatePostRequest createRequest = createPostRequest();
        MockHttpServletResponse createPostResponse = 게시글_작성(accessToken, createRequest);
        Long postId = getPostId(createPostResponse);

        String editContent = "hi #hi hello #hello #fashin #good #edit";
        EditPostRequest editPostRequest = new EditPostRequest(editContent, List.of("imageUrl3"));

        // when
        MockHttpServletResponse response = 게시글_수정(accessToken, editPostRequest, postId);

        // then
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @DisplayName("post 삭제 완료")
    @Test
    void delete_post_success() throws Exception {
        // given
        CreatePostRequest createRequest = createPostRequest();
        MockHttpServletResponse createPostResponse = 게시글_작성(accessToken, createRequest);
        Long postId = getPostId(createPostResponse);

        // when
        MockHttpServletResponse response = 게시글_삭제(accessToken, postId);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    private Long getPostId(MockHttpServletResponse response) {
        String[] locationHeader = response.getHeader(HttpHeaders.LOCATION).split("/");
        return Long.valueOf(locationHeader[locationHeader.length - 1]);
    }

    private CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
            .content("hi #hi hello #hello #fashin #good")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .build();
    }
}
