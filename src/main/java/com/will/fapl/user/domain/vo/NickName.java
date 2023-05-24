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
import org.apache.commons.lang3.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NickName {

    private static final int MAX_NICKNAME_LENGTH = 15;
    private static final String NICKNAME_FORMAT_REGEX = "[a-zA-Z0-9]+";
    private static final Pattern NICKNAME_FORMAT_PATTERN = Pattern.compile(NICKNAME_FORMAT_REGEX);

    @Column(name = "nickname", nullable = false)
    private String value;

    public NickName(String value) {
        validateNickname(value);
        this.value = value;
    }

    private void validateNickname(String value) {
        checkArgument(!StringUtils.isBlank(value), format("닉네임은 비어있을 수 없습니다. 현재 닉네임 : {0}", value));
        checkArgument(value.length() <= MAX_NICKNAME_LENGTH,
            format("닉네임은 {0}글자를 초과할 수 없습니다. 현재 닉네임 길이 : {1}", MAX_NICKNAME_LENGTH, value.length()));
        checkArgument(NICKNAME_FORMAT_PATTERN.matcher(value).matches(), format("닉네임은 영어와 숫자로 작성해주세요. 현재 닉네임 : {0}", value));
    }
}
