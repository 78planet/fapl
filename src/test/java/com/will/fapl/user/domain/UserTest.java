package com.will.fapl.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.will.fapl.point.domain.Point;
import com.will.fapl.user.domain.vo.Email;
import com.will.fapl.user.domain.vo.NickName;
import com.will.fapl.user.domain.vo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("멤버 생성 성공")
    @Test
    void createMember_success() {
        // given
        String email = "test@mail.com";
        String password = "!@Password123";
        String nickName = "qr123";

        // when
        User user = User.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, password))
            .nickName(new NickName(nickName))
            .grade(new Grade())
            .point(new Point(123L))
            .build();

        // then
        assertThat(user).isNotNull();
    }
}
