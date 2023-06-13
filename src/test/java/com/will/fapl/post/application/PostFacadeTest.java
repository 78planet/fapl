package com.will.fapl.post.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.domain.PostRepository;
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
