package com.will.fapl.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.will.fapl.hashtag.application.HashtagService;
import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.hashtag.domain.HashtagRepository;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.domain.Post;
import com.will.fapl.util.IntegrationTest;
import com.will.fapl.util.fixture.TestMemberBuilder;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class PostServiceTest extends IntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private HashtagService hashtagService;

    private Member member;
    @BeforeEach
    void setUp() {
        member = createMember();
    }

    @DisplayName("게시물 생성 성공")
    @Test
    void createPost_success() {
        // given
        CreatePostRequest request = createPostRequest();
        List<Hashtag> hashtags = List.of(
            new Hashtag("hi"),
            new Hashtag("new")
        );
        hashtagRepository.saveAll(hashtags);

        // when
        Long postId = postService.createPost(member, hashtags, request).getId();

        // then
        assertThat(postId).isPositive();
    }

    @DisplayName("게시글 단건 조회 성공")
    @Transactional
    @Test
    void getPostWithFetchById_success() {
        // given
        CreatePostRequest request = createPostRequest();
        List<Hashtag> hashtags = List.of(
            new Hashtag("hi"),
            new Hashtag("new")
        );
        hashtagRepository.saveAll(hashtags);
        Long postId = postService.createPost(member, hashtags, request).getId();
        entityManager.clear();

        // when
        Post post = postService.getPostWithFetchById(postId);

        // then
        assertThat(post.getPostImageList()).isNotNull();
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void modifyPost_success() {
        // given
        CreatePostRequest request = createPostRequest();
        List<String> hashtagStrs = List.of(
            "h1",
            "h2"
        );
        List<Hashtag> hashtags = hashtagService.createHashtag(hashtagStrs);
        Long postId = postService.createPost(member, hashtags, request).getId();

        List<String> editHashtagStrs = List.of(
            "h2",
            "h3",
            "h4",
            "h5"
        );
        List<Hashtag> edithashtags = hashtagService.createHashtag(editHashtagStrs);
        EditPostRequest editPostRequest = new EditPostRequest("edit content",
            List.of("imageUrl3", "imageUrl4"));

        // when
        Post updatedPost = postService.modifyPost(postId, edithashtags, editPostRequest);
        entityManager.clear();

        // then
        assertAll(
            () -> assertThat(postId).isEqualTo(updatedPost.getId()),
            () -> assertThat((long) updatedPost.getHashTagList().getPostHashtags().size()).isEqualTo(4),
            () -> assertThat(updatedPost.getPostImageList().getPostImages().get(0).getImageUrl()).isEqualTo("imageUrl3")
        );
    }

    private CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
            .content("hi #hi hello #hello #fashin #good")
            .imageUrls(List.of("imageUrl1", "imageUrl2"))
            .build();
    }

    private Member createMember() {
        Member member = new TestMemberBuilder().email("member1@gmail.com").build();
        memberRepository.save(member);
        return member;
    }
}
