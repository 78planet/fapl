package com.will.fapl.member.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.will.fapl.member.application.dto.SignupRequest;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일로 멤버 검색")
    @Test
    void getMemberByEmail_success() {
        // given
        String email = "test@gmail.com";
        Long memberId = memberService.createMember(new SignupRequest(email, "!@Aann123", "qsss"));

        // when
        Member member = memberService.getMemberByEmail(email);

        // then
        assertThat(member.getId()).isEqualTo(memberId);
    }

}
