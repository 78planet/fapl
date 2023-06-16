package com.will.fapl.comment.application.dto.request;

import com.will.fapl.comment.domain.Comment;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCommentRequest {

    private Long postId;
    private String comment;
    private Long hierarchy;
    private Long groupId;
    private Long orderNum;

    public CreateCommentRequest(Long postId, String comment, Long hierarchy, Long groupId, Long orderNum) {
        this.postId = postId;
        this.comment = comment;
        this.hierarchy = hierarchy;
        this.groupId = groupId;
        this.orderNum = orderNum;
    }

    public Comment toComment(Member member, Post post) {
        return Comment.builder()
            .post(post)
            .member(member)
            .content(comment)
            .hierarchy(hierarchy)
            .groupId(groupId)
            .orderNum(orderNum)
            .build();
    }
}
