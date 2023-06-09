package com.will.fapl.auth.domain;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("blackList")
public class BlackList {

    @Id
    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiredTime;

    public BlackList(String accessToken, long expiredTime) {
        this.accessToken = accessToken;
        this.expiredTime = expiredTime;
    }
}
