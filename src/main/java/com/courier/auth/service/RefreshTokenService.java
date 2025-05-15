package com.courier.auth.service;

import com.courier.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpiryMs;

    private static final String KEY_PREFIX = "refreshToken:";

    /**
     * 새 리프레시 토큰 생성 후 Redis에 저장
     * @param username id
     * @return
     */
    public String createRefreshToken(String username, String role) {
        // (1) 기존 토큰 삭제: username으로 된 모든 토큰 지움
        redisTemplate.keys(KEY_PREFIX + username + ":*")
                .forEach(redisTemplate::delete);

        // (2) 토큰 생성
        String token = jwtUtil.generateRefreshToken(username, role);

        // (3) Redis에 key=refreshToken:username:token, value=username, TTL 설정
        String redisKey = KEY_PREFIX + username + ":" + token;
        redisTemplate.opsForValue().set(redisKey, username, Duration.ofMillis(refreshExpiryMs));

        return token;
    }

    /**
     * 전달된 토큰이 Redis에 남아있고, 만료되지 않았는지 확인
     * @param username id
     * @param token 토큰
     * @return boolean
     */
    public boolean validateRefreshToken(String username, String token) {
        String redisKey = KEY_PREFIX + username + ":" + token;
        return redisTemplate.hasKey(redisKey);
    }

    /**
     * 특정 토큰 삭제(로그아웃 등)
     * @param username id
     * @param token 토큰
     */
    public void deleteRefreshToken(String username, String token) {
        String redisKey = KEY_PREFIX + username + ":" + token;
        redisTemplate.delete(redisKey);
    }
}
