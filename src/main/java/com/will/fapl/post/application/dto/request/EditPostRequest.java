package com.will.fapl.post.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditPostRequest {

    @Schema(description = "내용")
    @NotBlank
    private String content;

    @Schema(description = "이미지 저장 경로 리스트")
    @NotNull
    @Size(min = 1, max = 10, message = "이미지는 1개 이상 10개 이하로 등록이 가능합니다")
    private List<String> imageUrls;

    public EditPostRequest(String content, List<String> imageUrls) {
        this.content = content;
        this.imageUrls = imageUrls;
    }
}
