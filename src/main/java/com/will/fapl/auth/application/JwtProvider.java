package com.will.fapl.auth.application;

import com.will.fapl.auth.application.dto.TokenDto;
import java.util.Date;

public interface JwtProvider {

    TokenDto createAccessToken(String memberId);

    TokenDto createRefreshToken();

    boolean validateToken(String token);

    String getSubject(String token);

    Date getExpiredDate(String token);
}
