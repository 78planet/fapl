package com.will.fapl.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.application.dto.response.PostResponse;
import com.will.fapl.post.exception.NotFoundPostException;
import com.will.fapl.util.IntegrationTest;
import com.will.fapl.util.fixture.TestMemberBuilder;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostFacadeTest extends IntegrationTest {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("게시물 생성 성공")
    @Test
    void createPost_success() {
        // given
        LoginMember member = createLoginMember();
        CreatePostRequest request = createPostRequest();

        // when
        Long postId = postFacade.createPost(member, request);

        // then
        assertThat(postId).isPositive();
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void modifyPost_success() {
        // given
        LoginMember member = createLoginMember();
        CreatePostRequest request = createPostRequest();
        Long postId = postFacade.createPost(member, request);

        String editContent = "hi #hi hello #hello #fashin #good #edit";
        EditPostRequest editPostRequest = new EditPostRequest(editContent, List.of("imageUrl3"));

        // when
        Long editPostId = postFacade.modifyPost(member, postId, editPostRequest);

        // then
        PostResponse post = postFacade.getPost(editPostId);
        assertAll(
            () -> assertThat(postId).isEqualTo(editPostId),
            () -> assertThat(post.getContent()).isEqualTo(editContent),
            () -> assertThat(post.getPostImageList().get(0)).isEqualTo("imageUrl3")
        );
    }


    @DisplayName("게시물 삭제 성공")
    @Test
    void deletePost_success() {
        // given
        LoginMember member = createLoginMember();
        CreatePostRequest request = createPostRequest();
        Long postId = postFacade.createPost(member, request);

        // when
        postFacade.removePost(member.getId(), postId);

        // then
        assertThatThrownBy(() -> postFacade.getPost(postId))
            .isInstanceOf(NotFoundPostException.class)
            .hasMessageContaining("존재하지 않는 게시글입니다.");
    }

    private CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
            .content("hi #hi hello #hello #fashin #good")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .build();
    }

    private LoginMember createLoginMember() {
        Member member = new TestMemberBuilder().email("member1@gmail.com").build();
        memberRepository.save(member);
        return new LoginMember(member.getId(), "accessToken");
    }
}
