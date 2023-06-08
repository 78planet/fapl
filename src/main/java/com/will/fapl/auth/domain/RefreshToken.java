package com.will.fapl.auth.domain;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String id;
    private String value;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiredTime;

    public RefreshToken(String id, String value, long expiredTime) {
        this.id = id;
        this.value = value;
        this.expiredTime = expiredTime;
    }

    public boolean isNotSame(String refreshToken) {
        return !Objects.equals(this.value, refreshToken);
    }
}
