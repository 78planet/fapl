package com.will.fapl.post.application;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.hashtag.application.HashtagService;
import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.member.application.MemberService;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.application.dto.response.PostResponse;
import com.will.fapl.post.domain.Post;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final MemberService memberService;
    private final HashtagService hashtagService;

    @Transactional
    public Long createPost(LoginMember loginMember, CreatePostRequest createPostRequest) {
        Member member =  memberService.getMemberById(loginMember.getId());

        List<Hashtag> hashtagList = hashtagService.createHashtag(
            toHashTags(createPostRequest.getContent())
        );

        Post post = postService.createPost(member, hashtagList, createPostRequest);
        return post.getId();
    }

    public PostResponse getPost(Long postId) {
        Post post = postService.getPostWithFetchById(postId);
        return PostResponse.from(post);
    }

    @Transactional
    public Long modifyPost(LoginMember loginMember, Long postId, EditPostRequest editPostRequest) {
        Member member =  memberService.getMemberById(loginMember.getId());

        List<Hashtag> hashtagList = hashtagService.createHashtag(
            toHashTags(editPostRequest.getContent())
        );

        Post post = postService.modifyPost(postId, hashtagList , editPostRequest);
        return post.getId();
    }

    private List<String> toHashTags(String content) {
        return Arrays.stream(content.split("\\s+"))
            .filter(word -> word.startsWith("#"))
            .map(word -> word.replaceAll("[^\\p{L}\\p{N}]", ""))
            .collect(Collectors.toList());
    }
}
