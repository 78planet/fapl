package com.will.fapl.member.presentation;

import com.will.fapl.auth.aop.Login;
import com.will.fapl.auth.aop.LoginRequired;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.common.model.ApiResponse;
import com.will.fapl.member.application.MemberService;
import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.member.application.dto.request.EditProfileRequest;
import com.will.fapl.member.application.dto.response.MemberResponse;
import com.will.fapl.member.application.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Signup", description = "signup API")
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.createMember(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "멤버 정보 확인", description = "멤버 정보 확인 API입니다.")
    @LoginRequired
    @GetMapping
    public ResponseEntity<ApiResponse<MemberResponse>> showMember(@Login LoginMember loginMember) {
        MemberResponse memberResponse = memberService.getMemberResponseById(loginMember.getId());
        return ResponseEntity.ok(new ApiResponse<>(memberResponse));
    }

    @Operation(summary = "멤버 정보 수정", description = "멤버 정보 수정 API입니다.")
    @LoginRequired
    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> editProfile(@Valid @RequestBody EditProfileRequest request,
        @Login LoginMember loginMember) {
        ProfileResponse profileResponse = memberService.modifyMember(loginMember.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>(profileResponse));
    }

    @Operation(summary = "멤버 회원 탈퇴", description = "멤버 회원 탈퇴 API입니다.")
    @LoginRequired
    @DeleteMapping
    public ResponseEntity<Void> withdrawal(@Login LoginMember loginMember) {
        memberService.removeMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
