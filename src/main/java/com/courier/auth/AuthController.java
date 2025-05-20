package com.courier.auth;

import com.courier.auth.dto.AuthLoginDTO;
import com.courier.auth.dto.AuthRegisterDTO;
import com.courier.auth.dto.AuthResponse;
import com.courier.auth.service.AuthService;
import com.courier.auth.service.RefreshTokenService;
import com.courier.handler.exception.InvalidRefreshTokenException;
import com.courier.user.domain.User;
import com.courier.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Value("${cookie.secure:false}")
    private boolean cookieSecure;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginDTO dto, HttpServletResponse response) {

        // 인증 시도
        var authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        authManager.authenticate(authToken);

        // 사용자 정보 조회
        User user = authService.getUser(dto.getUsername());

        // JWT 발급
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), user.getRole());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)               // HTTPS 환경에서만 전송
                .path("/")                  // 애플리케이션 전체 경로
                .maxAge(7 * 24 * 60 * 60)   // 7일
                .sameSite("Strict")         // 또는 "Lax"
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(AuthResponse.of(user, accessToken));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterDTO dto, HttpServletResponse response) {

        User user = authService.saveUser(dto);
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), user.getRole());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)               // HTTPS 환경에서만 전송
                .path("/")                  // 애플리케이션 전체 경로
                .maxAge(7 * 24 * 60 * 60)   // 7일
                .sameSite("Strict")         // 또는 "Lax"
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        var body = AuthResponse.of(user, accessToken);
        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {

        // 1) 쿠키에서 기존 리프레시 토큰 꺼내기
        String oldToken = extractRefreshTokenFromCookie(request);
        if (oldToken == null) throw new InvalidRefreshTokenException("세션이 유효하지 않습니다.");

        String username = jwtUtil.getUsername(oldToken);
        if (!refreshTokenService.validateRefreshToken(username, oldToken)) {
            throw new InvalidRefreshTokenException("리프레시 토큰이 유효하지 않습니다.");
        }

        User user = authService.getUser(username);

        // 2) 검증 후, DB에서 oldToken 무효화(또는 삭제)
        refreshTokenService.deleteRefreshToken(username, oldToken);

        String role = jwtUtil.getRole(oldToken);
        // 3) 새로운 리프레시 토큰 생성
        String newRefreshToken = refreshTokenService.createRefreshToken(username, role);

        // 4) 새로운 액세스 토큰 생성
        String newAccess = jwtUtil.generateToken(username, role);

        // 5) HttpOnly 쿠키로 새로운 리프레시 토큰 설정
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(AuthResponse.of(user, newAccess));
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1) 쿠키에서 기존 리프레시 토큰 꺼내기
        String token = extractRefreshTokenFromCookie(request);
        if (token != null) {
            // 2) Redis에서 토큰 삭제
            String username = jwtUtil.getUsername(token);
            refreshTokenService.deleteRefreshToken(username, token);
        }

        // 3) HttpOnly 쿠키 만료 처리 (삭제)
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieSecure)    // login/logout 시와 동일한 secure 설정
                .path("/")
                .maxAge(0)               // 즉시 만료
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        // 4) 204 No Content 반환
        return ResponseEntity.noContent().build();
    }
}
