package com.will.fapl.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.will.fapl.member.exception.LoginFailedException;
import com.will.fapl.point.domain.Point;
import com.will.fapl.member.domain.vo.Email;
import com.will.fapl.member.domain.vo.NickName;
import com.will.fapl.member.domain.vo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("멤버 생성 성공")
    @Test
    void createMember_success() {
        // given
        String email = "test@mail.com";
        String password = "!@Password123";
        String nickName = "qr123";

        // when
        Member member = Member.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, password))
            .nickName(new NickName(nickName))
            .grade(Grade.BRONZE)
            .point(new Point(123L))
            .build();

        // then
        assertThat(member).isNotNull();
    }

    @DisplayName("비밀번호 일치 성공")
    @Test
    void checkPassword_success() {

        String email = "test@mail.com";
        String password = "!@Password123";
        String nickName = "fapl";

        Member member = Member.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, password))
            .nickName(new NickName(nickName))
            .build();

        assertDoesNotThrow(() -> member.checkPassword(passwordEncoder, password));
    }

    @DisplayName("비밀번호 일치 실패")
    @Test
    void checkPassword_fail() {

        String email = "test@mail.com";
        String password = "!@Password123";
        String nickName = "fapl";
        String notMyPassword = "!@Password1235";

        Member member = Member.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, password))
            .nickName(new NickName(nickName))
            .build();

        assertThatThrownBy(() -> member.checkPassword(passwordEncoder, notMyPassword))
            .isInstanceOf(LoginFailedException.class);
    }
}
