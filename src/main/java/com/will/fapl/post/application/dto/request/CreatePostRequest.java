package com.will.fapl.post.application.dto.request;

import com.will.fapl.hashtag.domain.Hashtag;
import com.will.fapl.member.domain.Member;
import com.will.fapl.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePostRequest {

    @Schema(description = "내용")
    @NotBlank
    private String content;

    @Schema(description = "이미지 저장 경로 리스트")
    @NotNull
    @Size(min = 1, max = 10, message = "이미지는 1개 이상 10개 이하로 등록이 가능합니다")
    private List<String> imageUrls;

    @Builder
    public CreatePostRequest(String content, List<String> imageUrls) {
        this.content = content;
        this.imageUrls = imageUrls;
    }

    public Post toPost(Member member, List<Hashtag> hashtags) {
        return Post.builder()
            .member(member)
            .content(content)
            .hashtags(hashtags)
            .likeCnt(0L)
            .dislikeCnt(0L)
            .postImages(imageUrls)
            .build();
    }
}
