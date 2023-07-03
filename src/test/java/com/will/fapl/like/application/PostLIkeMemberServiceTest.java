package com.will.fapl.like.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.member.application.MemberService;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import com.will.fapl.post.application.PostFacade;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.application.dto.response.PostSearchResponse;
import com.will.fapl.util.IntegrationTest;
import com.will.fapl.util.fixture.TestMemberBuilder;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostLIkeMemberServiceTest extends IntegrationTest {

    @Autowired
    private PostLikeMemberService postLikeMemberService;

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @DisplayName("좋아요한 게시글 불러오기 성공")
    @Test
    void getLikedPostList_success() {
        // given
        LoginMember loginMember = createLoginMember();

        CreatePostRequest request = createPostRequest();
        Long postId1 = postFacade.createPost(loginMember, request);
        postFacade.likePost(loginMember.getId(), postId1);
        Long postId2 = postFacade.createPost(loginMember, request);
        postFacade.likePost(loginMember.getId(), postId2);
        Member member = memberService.getMemberById(loginMember.getId());

        PostFilterCondition filterCondition = new PostFilterCondition(null, null, postId2);

        // when
        List<PostSearchResponse> likedPostList = postLikeMemberService.getLikedPostList(member, filterCondition);

        // then
        assertThat(likedPostList.size()).isEqualTo(1);
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
