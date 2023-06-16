package com.will.fapl.comment.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.will.fapl.comment.application.CommentFacade;
import com.will.fapl.comment.application.dto.request.CreateCommentRequest;
import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.util.AcceptanceTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

public class CommentControllerTest extends AcceptanceTest {

    private String accessToken;

    private Long postId;

    @BeforeEach
    void login() throws Exception {
        String email = "test@gmail.com";
        String password = "!Test1234";
        String nickName = "test";
        accessToken = 회원가입_토큰(new SignupRequest(email, password, nickName));
        CreatePostRequest createRequest = createPostRequest();
        MockHttpServletResponse response = 게시글_작성(accessToken, createRequest);
        postId = getPostId(response);
    }


    @DisplayName("comment 생성 완료")
    @Test
    void create_comment_success() throws Exception {
        // given
        CreateCommentRequest request = createCommentRequest(postId);

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andReturn().getResponse();

        // then
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.getHeader(HttpHeaders.LOCATION)).isNotNull()
        );
    }

    private CreateCommentRequest createCommentRequest(Long postId) {
        return new CreateCommentRequest(postId, "this is comment", 0L,
            0L, 0L);
    }

    private CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
            .content("hi #hi hello #hello #fashin #good")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .build();
    }

    private Long getPostId(MockHttpServletResponse response) {
        String[] locationHeader = response.getHeader(HttpHeaders.LOCATION).split("/");
        return Long.valueOf(locationHeader[locationHeader.length - 1]);
    }
}
