package com.courier.auth;

import com.courier.auth.dto.AuthLoginDTO;
import com.courier.auth.dto.AuthResponse;
import com.courier.auth.dto.TokenRefreshRequest;
import com.courier.auth.dto.TokenResponse;
import com.courier.auth.service.AuthService;
import com.courier.auth.service.RefreshTokenService;
import com.courier.handler.GlobalExceptionHandler;
import com.courier.handler.exception.InvalidRefreshTokenException;
import com.courier.user.domain.User;
import com.courier.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginDTO dto) {

        // 인증 시도
        var authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        authManager.authenticate(authToken);

        // 사용자 정보 조회
        User user = authService.getUser(dto.getUsername());

        // JWT 발급
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(user, accessToken, refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthLoginDTO dto) {

        User user = authService.saveUser(dto);
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), user.getRole());

        var body = new AuthResponse(user, accessToken, refreshToken);
        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @Valid @RequestBody TokenRefreshRequest req
    ) {
        String token = req.refreshToken();
        // JWT 에서 사용자명 추출
        String username = jwtUtil.getUsername(token);

        if (!refreshTokenService.validateRefreshToken(username, token)) {
            throw new InvalidRefreshTokenException("리프레시 토큰이 유효하지 않습니다.");
        }

        String role = jwtUtil.getRole(token);
        String newAccess = jwtUtil.generateToken(username, role);
        return ResponseEntity.ok(
                new TokenResponse("Bearer", newAccess, token)  // refresh token 재발급 안 할 땐 기존 토큰 재사용
        );
    }

}
