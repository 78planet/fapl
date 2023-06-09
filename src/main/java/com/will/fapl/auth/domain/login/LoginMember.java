package com.will.fapl.auth.domain.login;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LoginMember {

    private Long id;
    private String accessToken;

    public LoginMember(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }

    public LoginMember() {
        this(null, null);
    }
}
