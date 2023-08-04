package com.will.fapl.like.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.vo.Email;
import com.will.fapl.member.domain.vo.NickName;
import com.will.fapl.member.domain.vo.Password;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.domain.Post;
import com.will.fapl.util.fixture.TestMemberBuilder;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostLikeMemberTest {

    @DisplayName("PostLikeMember 생성 성공")
    @Test
    void postLikeMemberCreate_success() {
        Member member = createMember();
        Post post = createPost(member);

        PostLikeMember postLikeMember = new PostLikeMember(member, post);

        assertThat(postLikeMember).isNotNull();
        assertThat(postLikeMember.getMember()).isNotNull();
        assertThat(postLikeMember.getPost()).isNotNull();
    }

    @DisplayName("PostDislikeMember 생성 성공")
    @Test
    void postDislikeMemberCreate_success() {
        Member member = createMember();
        Post post = createPost(member);

        PostDislikeMember postDislikeMember = new PostDislikeMember(member, post);

        assertThat(postDislikeMember).isNotNull();
        assertThat(postDislikeMember.getMember()).isNotNull();
        assertThat(postDislikeMember.getPost()).isNotNull();
    }

    private Member createMember() {
        return new TestMemberBuilder().email("member1@gmail.com").build();
    }

    private Post createPost(Member member) {
        List<String> imageUrls = List.of(
            "imageUrl1",
            "imageUrl2",
            "imageUrl3",
            "imageUrl4",
            "imageUrl5",
            "imageUrl6"
        );

        return Post.builder()
            .member(member)
            .content("this is content #sdaf")
            .hashtags(null)
            .likeCnt(123L)
            .dislikeCnt(2L)
            .postImages(imageUrls)
            .build();
    }
}
