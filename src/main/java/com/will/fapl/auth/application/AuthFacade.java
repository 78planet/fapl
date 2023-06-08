package com.will.fapl.auth.application;

import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.application.dto.request.SignInRequest;
import com.will.fapl.auth.application.dto.response.AccessTokenResponse;
import com.will.fapl.auth.application.dto.response.LoginMemberResponse;
import com.will.fapl.auth.application.dto.response.LoginResponseDto;
import com.will.fapl.auth.domain.RefreshToken;
import com.will.fapl.auth.domain.login.LoginMember;
import com.will.fapl.auth.exception.InvalidTokenException;
import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.member.application.MemberService;
import com.will.fapl.member.domain.Member;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final BlackListService blackListService;

    @Transactional
    public LoginResponseDto login(SignInRequest signInRequest) {
        Member member = memberService.getMemberByEmail(signInRequest.getEmail());
        member.checkPassword(passwordEncoder, signInRequest.getPassword());

        TokenDto accessToken = createAccessToken(member);
        TokenDto refreshToken = jwtProvider.createRefreshToken();

        refreshTokenService.saveToken(String.valueOf(member.getId()), refreshToken);
        return new LoginResponseDto(accessToken, refreshToken, LoginMemberResponse.from(member));
    }

    private TokenDto createAccessToken(Member member) {
        String memberId = String.valueOf(member.getId());
        return jwtProvider.createAccessToken(memberId);
    }

    public void logout(LoginMember member) {
        String memberId = String.valueOf(member.getId());
        String accessToken = member.getAccessToken();

        registerBlackList(accessToken);
        refreshTokenService.removeByMemberId(memberId);
    }

    private void registerBlackList(String accessToken) {
        Date expiredDate = jwtProvider.getExpiredDate(accessToken);
        TokenDto tokenDto = new TokenDto(accessToken, expiredDate.getTime() - new Date().getTime());

        blackListService.saveBlackList(tokenDto);
    }

    public AccessTokenResponse refreshAccessToken(String accessToken, String refreshToken) {
        String memberId = jwtProvider.getSubject(accessToken);
        validateRefreshToken(memberId, refreshToken);

        String newAccessToken = createNewAccessToken(memberId);
        return new AccessTokenResponse(newAccessToken);
    }

    private String createNewAccessToken(String memberId) {
        return jwtProvider.createAccessToken(memberId).getValue();
    }

    private void validateRefreshToken(String memberId, String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
        }

        RefreshToken findRefreshToken = getRefreshToken(memberId);
        if (!findRefreshToken.isSame(refreshToken)) {
            throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
        }
    }

    private RefreshToken getRefreshToken(String memberId) {
        return refreshTokenService.getTokenByMemberId(memberId);
    }
}
