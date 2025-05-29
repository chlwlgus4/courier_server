package com.courier.user;

import com.courier.config.security.CustomUserDetails;
import com.courier.user.dto.EmailModifyRequest;
import com.courier.user.dto.PasswordChangeRequest;
import com.courier.user.dto.UserResponse;
import com.courier.user.dto.UsernameCheckResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    @Value("${cookie.secure:false}")
    private boolean cookieSecure;

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserDetails principal) {
        String username = principal.getUsername();
        UserResponse res = userService.getUser(username);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/check-username")
    public ResponseEntity<UsernameCheckResponse> checkUsername(@RequestParam String username) {
        UsernameCheckResponse available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(available);
    }

    @PatchMapping("/modify-email")
    public ResponseEntity<UserResponse> modifyEmail(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody @Valid EmailModifyRequest req) {
        UserResponse res = userService.modifyEmail(principal.getId(), req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/password-change")
    public ResponseEntity<?> passwordChange(@AuthenticationPrincipal UserDetails principal,
                                            @RequestBody @Valid PasswordChangeRequest req,
                                            HttpServletResponse response) {
        String username = principal.getUsername();
        userService.passwordChange(username, req);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieSecure)    // login/logout 시와 동일한 secure 설정
                .path("/")
                .maxAge(0)               // 즉시 만료
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok().build();
    }

}
