package com.will.fapl.member.application;

import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.member.application.dto.request.EditProfileRequest;
import com.will.fapl.member.application.dto.response.MemberResponse;
import com.will.fapl.member.application.dto.response.ProfileResponse;
import com.will.fapl.member.exception.NotFoundMemberException;
import com.will.fapl.point.domain.Point;
import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.member.domain.Grade;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import com.will.fapl.member.domain.vo.Email;
import com.will.fapl.member.domain.vo.NickName;
import com.will.fapl.member.domain.vo.Password;
import com.will.fapl.member.exception.DuplicateEmailException;
import com.will.fapl.member.exception.LoginFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createMember(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        validateExistEmail(email);

        Member member = Member.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, signupRequest.getPassword()))
            .nickName(new NickName(signupRequest.getNickName()))
            .grade(Grade.BRONZE)
            .point(new Point(0L))
            .build();
        return memberRepository.save(member).getId();
    }

    @Transactional
    public ProfileResponse modifyMember(Long id, EditProfileRequest editProfileRequest) {
        Member member = getMemberById(id);
        String nickName = editProfileRequest.getNickName();
        String imageUrl = editProfileRequest.getImageUrl();

        member.changeNickName(nickName);
        member.changeProfileImage(imageUrl);
        return new ProfileResponse(imageUrl, nickName);
    }

    @Transactional
    public void removeMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    private void validateExistEmail(String email) {
        if (memberRepository.existsByEmailValue(email)) {
            throw new DuplicateEmailException(email, ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new LoginFailedException(ErrorCode.LOGIN_FAILED));
    }

    public MemberResponse getMemberResponseById(Long id) {
        Member member = getMemberById(id);
        return MemberResponse.from(member);
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER, memberId));
    }

    public Member getMemberWithFetchById(Long memberId) {
        return memberRepository.findMemberWithFetchById(memberId)
            .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER, memberId));
    }
}
