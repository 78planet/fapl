package com.will.fapl.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.member.domain.Member;
import com.will.fapl.util.fixture.TestMemberBuilder;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostHashtagTest {

    @DisplayName("PostHashtag 생성 성공")
    @Test
    void postHashtagCreate_success() {
        Member member = createMember();

        PostHashtag postHashtag = new PostHashtag(createPost(member), new Hashtag("hi"));

        assertThat(postHashtag).isNotNull();
        assertThat(postHashtag.getPost()).isNotNull();
        assertThat(postHashtag.getHashtag()).isNotNull();
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
