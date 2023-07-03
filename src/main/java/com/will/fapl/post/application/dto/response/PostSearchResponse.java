package com.will.fapl.post.application.dto.response;

import com.will.fapl.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearchResponse {

    @Schema(description = "게시글 아이디")
    private Long postId;

    @Schema(description = "썸네일 사진")
    private String imageUrl;

    @Schema(description = "작성일")
    private LocalDateTime createDateTime;

    @Schema(description = "좋아요 수")
    private Long likeCnt;

    @Schema(description = "싫어요 수")
    private Long dislikeCnt;

    @Builder
    public PostSearchResponse(Long postId, String imageUrl, LocalDateTime createDateTime,
                                Long likeCnt, Long dislikeCnt) {
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.createDateTime = createDateTime;
        this.likeCnt = likeCnt;
        this.dislikeCnt = dislikeCnt;
    }

    public static List<PostSearchResponse> from(List<Post> posts) {
        return posts.stream().map(post -> new PostSearchResponse(
            post.getId(),
            post.getThumbnailUrl(),
            post.getCreatedAt(),
            post.getLikeCnt().getValue(),
            post.getDislikeCnt().getValue()
        )).collect(Collectors.toList());
    }
}
