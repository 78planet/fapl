package com.will.fapl.auth.infra;

import com.will.fapl.auth.application.JwtProvider;
import com.will.fapl.auth.application.dto.TokenDto;
import com.will.fapl.auth.exception.InvalidTokenException;
import com.will.fapl.common.exception.ErrorCode;
import com.will.fapl.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements JwtProvider {

    private static final String MEMBER_CLAIM_NAME = "memberId";

    private final String issuer;
    private final Key secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token.expire-length}") long accessTokenValidityInMilliseconds,
        @Value("${jwt.refresh-token.expire-length}") long refreshTokenValidityInMilliseconds) {

        this.issuer = issuer;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    @Override
    public TokenDto createAccessToken(String memberId) {
        String token = createToken(memberId, accessTokenValidityInMilliseconds);
        return new TokenDto(token, accessTokenValidityInMilliseconds);
    }

    @Override
    public TokenDto createRefreshToken() {
        String token = createToken(UUID.randomUUID().toString(), refreshTokenValidityInMilliseconds);
        return new TokenDto(token, refreshTokenValidityInMilliseconds);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Date getExpiredDate(String token) {
        return getClaims(token).getExpiration();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(token, ErrorCode.INVALID_TOKEN);
        }
    }

    private String createToken(String memberId, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setSubject(memberId)
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }
}
