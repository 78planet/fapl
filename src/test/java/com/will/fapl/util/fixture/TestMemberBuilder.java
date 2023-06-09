package com.will.fapl.util.fixture;

import com.will.fapl.member.domain.Grade;
import com.will.fapl.member.domain.Member;
import com.will.fapl.member.domain.vo.Email;
import com.will.fapl.member.domain.vo.NickName;
import com.will.fapl.member.domain.vo.Password;
import com.will.fapl.point.domain.Point;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestMemberBuilder {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private Email email = new Email("test@gmail.com");
    private Password password = Password.encryptPassword(PASSWORD_ENCODER, "!Test1234");
    private NickName nickName = new NickName("test");
    private String imageUrl = "test";
    private Point point = new Point(0L);
    private Grade grade = Grade.BRONZE;

    public TestMemberBuilder email(String email) {
        this.email = new Email(email);
        return this;
    }

    public TestMemberBuilder password(String password) {
        this.password = Password.encryptPassword(PASSWORD_ENCODER, password);
        return this;
    }

    public TestMemberBuilder nickName(String nickName) {
        this.nickName = new NickName(nickName);
        return this;
    }

    public TestMemberBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public TestMemberBuilder point(Point point) {
        this.point = point;
        return this;
    }


    public TestMemberBuilder garde(Grade grade) {
        this.grade = grade;
        return this;
    }

    public Member build() {
        return Member.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .imageUrl(imageUrl)
            .point(point)
            .grade(grade)
            .build();
    }
}
