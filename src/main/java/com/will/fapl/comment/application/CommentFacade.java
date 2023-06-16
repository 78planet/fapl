package com.will.fapl.comment.application;

import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.comment.application.dto.request.CreateCommentRequest;
import com.will.fapl.comment.domain.Comment;
import com.will.fapl.member.application.MemberService;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.application.PostService;
import com.will.fapl.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentFacade {

    private final MemberService memberService;
    private final CommentService commentService;
    private final PostService postService;

    @Transactional
    public Long createComment(LoginMember loginMember, CreateCommentRequest createCommentRequest) {
        Member member =  memberService.getMemberById(loginMember.getId());
        Post post = postService.getPostById(createCommentRequest.getPostId());

        Comment comment = commentService.createComment(member, post, createCommentRequest);
        return comment.getId();
    }


}
