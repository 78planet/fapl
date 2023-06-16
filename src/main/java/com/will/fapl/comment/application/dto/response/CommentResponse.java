package com.will.fapl.comment.application.dto.response;

import com.will.fapl.member.application.dto.response.MemberResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {

    private Long id;
    private MemberResponse member;
    private String comment;
    private Long hierarchy;
    private Long groupId;
    private Long orderNum;

    public CommentResponse(Long id, MemberResponse member, String comment, Long hierarchy, Long groupId, Long orderNum) {
        this.id = id;
        this.member = member;
        this.comment = comment;
        this.hierarchy = hierarchy;
        this.groupId = groupId;
        this.orderNum = orderNum;
    }
}
