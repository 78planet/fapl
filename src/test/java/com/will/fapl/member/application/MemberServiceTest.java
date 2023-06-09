package com.will.fapl.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.member.application.dto.request.EditProfileRequest;
import com.will.fapl.member.application.dto.response.MemberResponse;
import com.will.fapl.member.application.dto.response.ProfileResponse;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import com.will.fapl.member.exception.DuplicateEmailException;
import com.will.fapl.member.exception.LoginFailedException;
import com.will.fapl.member.exception.NotFoundMemberException;
import com.will.fapl.util.IntegrationTest;
import com.will.fapl.util.fixture.TestMemberBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class MemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 생성 성공")
    @Test
    void createMember_success() {
        // given
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "!@Test11123", "fapll");

        // when
        Long id = memberService.createMember(signupRequest);

        // then
        assertThat(id).isPositive();
    }

    @DisplayName("중복 이메일로 인한 회원 생성 실패")
    @Test
    void createMember_duplicateEmail_fail() {
        // given
        String email = "test@gmail.com";
        memberService.createMember(new SignupRequest(email, "!@Test11123", "fapll"));
        SignupRequest duplicateEmailRequest = new SignupRequest(email, "!@Test123", "fapll");

        // when
        // then
        assertThatThrownBy(() -> memberService.createMember(duplicateEmailRequest))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("이메일로 회원 찾기 성공")
    @Test
    void getMemberByEmail_success() {
        // given
        String email = "test@gmail.com";
        Long memberId = memberService.createMember(new SignupRequest(email, "!@Test11123", "fapll"));

        // when
        Member member = memberService.getMemberByEmail(email);

        // then
        assertThat(member.getId()).isEqualTo(memberId);
    }

    @DisplayName("저장되지 않는 이메일로 인한 회원 찾기 실패")
    @Test
    void getMemberByEmail_notFoundEmail_fail() {
        // given
        memberService.createMember(new SignupRequest("test@gmail.com", "!@Test11123", "fapll"));

        // when
        // then
        assertThatThrownBy(() -> memberService.getMemberByEmail("tt@gmail.com"))
            .isInstanceOf(LoginFailedException.class)
            .hasMessageContaining("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("회원 정보 조회 성공")
    @Test
    void getMemberResponse_solo_success() {
        // given
        Member member = new TestMemberBuilder().email("test2@gmail.com").build();
        Long id = memberRepository.save(member).getId();

        // when
        MemberResponse memberResponse = memberService.getMemberResponseById(id);

        // then
        assertAll(
            () -> assertThat(memberResponse.getNickName()).isNotNull(),
            () -> assertThat(memberResponse.getImageUrl()).isNotNull()
        );
    }

    @DisplayName("회원 프로필 수정 성공")
    @Test
    void modifyMember_success() {
        // given
        Long id = memberRepository.save(new TestMemberBuilder().build()).getId();
        String imageUrl = "modifyUrl";
        String nickName = "modifyNickName";
        EditProfileRequest editProfileRequest = new EditProfileRequest(imageUrl, nickName);

        // when
        ProfileResponse profile = memberService.modifyMember(id, editProfileRequest);

        // then
        assertAll(
            () -> assertThat(profile.getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(profile.getNickName()).isEqualTo(nickName)
        );
    }

    @DisplayName("존재하지 않는 회원으로 인한 프로필 수정 실패")
    @Test
    void modifyMember_notFoundMember_success() {
        // given
        String imageUrl = "modifyUrl";
        String nickName = "modifyNickName";
        EditProfileRequest editProfileRequest = new EditProfileRequest(imageUrl, nickName);

        // when
        // then
        assertThatThrownBy(() -> memberService.modifyMember(1L, editProfileRequest))
            .isInstanceOf(NotFoundMemberException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_MEMBER)
            .hasMessageContaining("존재하지 않는 회원입니다.");
    }

    @DisplayName("회원 삭제 성공")
    @Test
    void removeMember_success() {
        // given
        String email = "test@gmail.com";
        Long id = memberRepository.save(new TestMemberBuilder().email(email).build()).getId();

        // when
        memberService.removeMember(id);

        // then
        assertThatThrownBy(() -> memberService.getMemberResponseById(id))
            .isInstanceOf(NotFoundMemberException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_MEMBER)
            .hasMessageContaining("존재하지 않는 회원입니다.");
    }
}
