package com.will.fapl.auth.application;

import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.domain.RefreshToken;
import com.will.fapl.auth.domain.RefreshTokenRepository;
import com.will.fapl.auth.exception.InvalidTokenException;
import com.will.fapl.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveToken(String memberId, TokenDto tokenDto) {
        RefreshToken refreshToken = new RefreshToken(memberId, tokenDto.getValue(), tokenDto.getExpiredTime());
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void removeByMemberId(String memberId) {
        refreshTokenRepository.deleteById(memberId);
    }

    public RefreshToken getTokenByMemberId(String memberId) {
        return refreshTokenRepository.findById(memberId)
            .orElseThrow(() -> new InvalidTokenException(null, ErrorCode.INVALID_TOKEN));
    }
}
