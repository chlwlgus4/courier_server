package com.courier.user;

import com.courier.user.domain.User;
import com.courier.user.dto.UserResponse;
import com.courier.user.dto.UserLoginDTO;
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

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserLoginDTO dto) {

        // 인증 시도
        var authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        authManager.authenticate(authToken);

        // 사용자 정보 조회
        User user = userService.getUser(dto.getUsername());

        // JWT 발급
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new UserResponse(user, token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserLoginDTO dto) {

        User user = userService.saveUser(dto);
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new UserResponse(user, token));
    }

}
