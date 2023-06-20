package com.will.fapl.post.application;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.hashtag.application.HashtagService;
import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.member.application.MemberService;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.application.dto.response.PostResponse;
import com.will.fapl.post.application.dto.response.PostSearchResponse;
import com.will.fapl.post.domain.Post;
import com.will.fapl.post.exception.PostNotBelongToCoupleException;
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


    public List<PostSearchResponse> searchPosts(PostFilterCondition postFilterCondition) {
        return postService.searchPostsByHashtag(postFilterCondition);
    }

    @Transactional
    public Long modifyPost(LoginMember loginMember, Long postId, EditPostRequest editPostRequest) {
        memberService.getMemberById(loginMember.getId());

        List<Hashtag> hashtagList = hashtagService.createHashtag(
            toHashTags(editPostRequest.getContent())
        );

        Post post = postService.modifyPost(postId, hashtagList , editPostRequest);
        return post.getId();
    }

    @Transactional
    public void removePost(Long memberId, Long postId) {
        Member member =  memberService.getMemberById(memberId);
        Post post = postService.getPostById(postId);
        if (!post.isWrittenBy(member)) {
            throw new PostNotBelongToCoupleException(memberId, postId, ErrorCode.NOT_BELONG_TO_COUPLE);
        }
        postService.removePost(postId);
    }

    private List<String> toHashTags(String content) {
        return Arrays.stream(content.split("\\s+"))
            .filter(word -> word.startsWith("#"))
            .map(word -> word.replaceAll("[^\\p{L}\\p{N}]", ""))
            .collect(Collectors.toList());
    }

}
