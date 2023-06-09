package com.will.fapl.member.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequest {

    @Schema(description = "이메일")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    private String email;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;

    @NotBlank(message = "닉네임은 비어있을 수 없습니다.")
    private String nickName;

    public SignupRequest(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
}
