package com.will.fapl.post.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostFilterCondition {

    @Schema(description = "해시태그")
    private String hashtag;

    @Schema(description = "멤버 닉네임")
    private String memberNickname;

    @Schema(description = "마지막 게시글 아이디")
    private Long lastPostId;

    @Schema(description = "사이즈")
    private int paginationSize = 20;

    public PostFilterCondition(String hashtag, String memberNickname, Long lastPostId) {
        this.hashtag = hashtag;
        this.memberNickname = memberNickname;
        this.lastPostId = lastPostId;
    }
}
