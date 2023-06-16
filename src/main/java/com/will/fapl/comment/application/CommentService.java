package com.will.fapl.comment.application;

import com.will.fapl.comment.application.dto.request.CreateCommentRequest;
import com.will.fapl.comment.domain.Comment;
import com.will.fapl.comment.domain.CommentRepository;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Member member, Post post, CreateCommentRequest createCommentRequest) {
        Comment comment = createCommentRequest.toComment(member, post);
        return commentRepository.save(comment);
    }
}
