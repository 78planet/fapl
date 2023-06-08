package com.will.fapl.member.application.dto.request;

import com.will.fapl.comment.validator.ImageUrl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditProfileRequest {

    @Schema(description = "프로필 이미지 URL")
    @ImageUrl
    private String imageUrl;

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임은 비어있을 수 없습니다.")
    private String nickName;

    public EditProfileRequest(String imageUrl, String nickName) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
    }
}
