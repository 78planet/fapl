package com.will.fapl.member.application.dto.response;


import com.will.fapl.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    @Schema(description = "회원 번호")
    private Long id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이미지 URL")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    public MemberResponse(Long id, String email, String imageUrl, String nickName) {
        this.id = id;
        this.email = email;
        this.imageUrl = imageUrl;
        this.nickName = nickName;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getEmail().getValue(),
            member.getProfileImage(),
            member.getNickName().getValue()
        );
    }
}
