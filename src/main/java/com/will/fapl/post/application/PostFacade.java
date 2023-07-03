package com.will.fapl.post.application;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.hashtag.application.HashtagService;
import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.like.application.PostDislikeMemberService;
import com.will.fapl.like.application.PostLikeMemberService;
import com.will.fapl.member.application.MemberService;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.dto.request.CreatePostRequest;
import com.will.fapl.post.application.dto.request.EditPostRequest;
import com.will.fapl.post.application.dto.request.PostFilterCondition;
import com.will.fapl.post.application.dto.response.PostResponse;
import com.will.fapl.post.application.dto.response.PostSearchResponse;
import com.will.fapl.post.domain.Post;
import com.will.fapl.post.exception.AlreadyDislikedPost;
import com.will.fapl.post.exception.AlreadyLikedPost;
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
    private final PostLikeMemberService postLikeMemberService;
    private final PostDislikeMemberService postDislikeMemberService;

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

    @Transactional
    public void likePost(Long memberId, Long postId) {
        // 1. 멤버 정보 불러오기
        Member member = memberService.getMemberById(memberId);
        Post post = postService.getPostById(postId);
        Member postOwner = memberService.getMemberById(post.getMember().getId());

        // 2. 내가 이 post에 좋아요나 싫어요 반응이 있는지 확인.
        if (postLikeMemberService.isLikePost(member, post)) {
            // 2-1. 좋아요 반응이 이미 있으면 '이미 좋아요 눌렀습니다'
            throw new AlreadyLikedPost(member.getId(), post.getId(), ErrorCode.ALREADY_LIKED_POST);

        } else if (postDislikeMemberService.isDislikePost(member, post)) {
            // 2-2. 싫어요 반응이 이미 있으면 싫어요 반응을 삭제하고 좋아요 반응으로 수정.
            // 2-2-1. 싫어요 게시글 리스트에서 게시글 삭제
            postDislikeMemberService.cancelDislike(member, post);

            // 2-2-2. 해당 게시글 싫어요 카운트 감소, 게시글 주인 싫어요 포인트 취소
            post.minusDislikeCnt();
            postOwner.cancelDislikePoint();

            // 2-2-3. 해당 게시글 좋아여 카운트 증가
            post.plusLikeCnt();

        } else {
            // 2-3. 아무 반응도 없으면 post 에 like cnt 추가하기
            post.plusLikeCnt();
        }

        // 3. 좋아요 리스트에 해당 게시글 추가하기.
        postLikeMemberService.addPost(member, post);

        // 4. like point 추가
        postOwner.addLikePoint();
    }

    @Transactional
    public void dislikePost(Long memberId, Long postId) {
        // 1. 멤버 정보 불러오기
        Member member =  memberService.getMemberById(memberId);
        Post post = postService.getPostById(postId);
        Member postOwner = memberService.getMemberById(post.getMember().getId());

        // 2. 내가 이 post에 좋아요나 싫어요 반응이 있는지 확인.
        if (postDislikeMemberService.isDislikePost(member, post)) {
            // 2-1. 싫어요 반응이 이미 있으면 '이미 싫어요 눌렀습니다'
            throw new AlreadyDislikedPost(member.getId(), post.getId(), ErrorCode.ALREADY_DISLIKED_POST);

        } else if (postLikeMemberService.isLikePost(member, post)) {
            // 2-2. 좋아요 반응이 이미 있으면 좋아요 반응을 삭제하고 싫어요 반응으로 수정.
            // 2-2-1. 좋아요 게시글 리스트에서 게시글 삭제
            postLikeMemberService.cancelLike(member, post);

            // 2-2-2. 해당 게시글 좋아요 카운트 감소, 게시글 주인 좋아요 포인트 취소
            post.minusLikeCnt();
            postOwner.cancelLikePoint();

            // 2-2-3. 해당 게시글 싫어요 카운트 증가
            post.plusDislikeCnt();

        } else {
            // 2-3. 아무 반응도 없으면 post 에 dislike cnt 추가하기
            post.plusDislikeCnt();
        }

        // 3. 좋아요 리스트에 해당 게시글 추가하기.
        postDislikeMemberService.addPost(member, post);

        // 4. dislike point 추가
        postOwner.addDislikePoint();
    }

    @Transactional
    public void cancelLike(Long memberId, Long postId) {
        Member member =  memberService.getMemberById(memberId);
        Post post = postService.getPostById(postId);
        Member postOwner = memberService.getMemberById(post.getMember().getId());

        // 좋아요 반응이 이미 있으면 좋아요 취소
        if (postLikeMemberService.isLikePost(member, post)) {

            // 좋아요 리스트에서 제거
            postLikeMemberService.cancelLike(member, post);

            // 해당 게시글 좋아요개수 감소
            post.minusLikeCnt();

            // 해당 게시글을 가지고 있는 유저의 포인트 감소.
            postOwner.cancelLikePoint();
        }
    }

    @Transactional
    public void cancelDislike(Long memberId, Long postId) {
        Member member =  memberService.getMemberById(memberId);
        Post post = postService.getPostById(postId);
        Member postOwner = memberService.getMemberById(post.getMember().getId());

        // 싫어요 반응이 이미 있으면 싫어요 취소
        if (postDislikeMemberService.isDislikePost(member, post)) {

            // 싫어요 리스트에서 제거
            postDislikeMemberService.cancelDislike(member, post);

            // 해당 게시글 싫어요 개수 감소
            post.minusDislikeCnt();

            // 해당 게시글을 가지고 있는 유저의 포인트 감소.
            postOwner.cancelDislikePoint();
        }
    }

    private List<String> toHashTags(String content) {
        return Arrays.stream(content.split("\\s+"))
            .filter(word -> word.startsWith("#"))
            .map(word -> word.replaceAll("[^\\p{L}\\p{N}]", ""))
            .collect(Collectors.toList());
    }

    public List<PostSearchResponse> getLikedPostList(Long memberId, PostFilterCondition postFilterCondition) {
        Member member =  memberService.getMemberById(memberId);
        return postLikeMemberService.getLikedPostList(member, postFilterCondition);
    }
}
