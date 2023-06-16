package com.will.fapl.comment.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.comment.application.dto.request.CreateCommentRequest;
import com.will.fapl.comment.domain.Comment;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import com.will.fapl.post.application.PostFacade;
import com.will.fapl.post.application.PostService;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.domain.Post;
import com.will.fapl.util.IntegrationTest;
import com.will.fapl.util.fixture.TestMemberBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentServiceTest extends IntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostService postService;

    @Test
    void createComment_success() {
        // given
        LoginMember loginMember = createMember();
        CreatePostRequest request = createPostRequest();
        Long postId = postFacade.createPost(loginMember, request);

        Member member = memberRepository.findById(loginMember.getId()).get();
        Post post = postService.getPostById(postId);
        CreateCommentRequest commentRequest = new CreateCommentRequest(postId, "this is comment", 0L,
            0L, 0L);

        // when
        Comment comment = commentService.createComment(member, post, commentRequest);

        // then
        assertThat(comment.getId()).isPositive();
    }

    private CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
            .content("hi #hi hello #hello #fashin #good")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .build();
    }

    private LoginMember createMember() {
        Member member = new TestMemberBuilder().email("member1@gmail.com").build();
        memberRepository.save(member);
        return new LoginMember(member.getId(), "accessToken");
    }
}
