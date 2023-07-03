package com.will.fapl.post.application.dto.response;

import com.will.fapl.comment.domain.application.dto.response.CommentResponse;
import com.will.fapl.member.application.dto.response.MemberResponse;
import com.will.fapl.post.domain.Post;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponse {

    private Long id;
    private MemberResponse member;
    private String content;
    private Long likeCnt;
    private Long dislikeCnt;
    private List<String> postImageList;
    private List<CommentResponse> comments;

    @Builder
    public PostResponse(Long id, MemberResponse member, String content, Long likeCnt,
                        Long dislikeCnt, List<String> postImageList,
                        List<CommentResponse> comments) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.likeCnt = likeCnt;
        this.dislikeCnt = dislikeCnt;
        this.postImageList = postImageList;
        this.comments = comments;
    }

    public static PostResponse from(Post post) {
        return PostResponse.builder()
            .id(post.getId())
            .member(MemberResponse.from(post.getMember()))
            .content(post.getContent())
            .likeCnt(post.getLikeCnt().getValue())
            .dislikeCnt(post.getDislikeCnt().getValue())
            .postImageList(post.getPostImageList().getImageUrls())
            .build();
    }
}
