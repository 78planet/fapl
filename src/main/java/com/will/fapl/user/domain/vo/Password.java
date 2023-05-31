package com.will.fapl.user.domain.vo;

import static com.google.common.base.Preconditions.checkArgument;
import static java.text.MessageFormat.format;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    private static final String PASSWORD_FORMAT_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W])).*";
    private static final Pattern PASSWORD_FORMAT_PATTERN = Pattern.compile(PASSWORD_FORMAT_REGEX);
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;

    @Column(name = "password", nullable = false)
    private String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password encryptPassword(PasswordEncoder passwordEncoder, String value) {
        validatePassword(value);
        return new Password(passwordEncoder.encode(value));
    }

    private static void validatePassword(String value) {
        checkArgument(MIN_LENGTH <= value.length() && value.length() <= MAX_LENGTH,
            format("비밀번호는 {0}자 이상 {1}자 이하입니다.", MIN_LENGTH, MAX_LENGTH));
        checkArgument(PASSWORD_FORMAT_PATTERN.matcher(value).matches(), "비밀번호는 대소문자, 숫자, 특수 문자를 포함해야 생성 가능합니다.");
    }

    public boolean isSamePassword(PasswordEncoder passwordEncoder, String inputPassword) {
        return passwordEncoder.matches(inputPassword, this.value);
    }
}
