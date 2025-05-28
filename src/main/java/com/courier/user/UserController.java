package com.courier.user;

import com.courier.config.security.CustomUserDetails;
import com.courier.user.dto.EmailModifyRequest;
import com.courier.user.dto.PasswordChangeRequest;
import com.courier.user.dto.UserResponse;
import com.courier.user.dto.UsernameCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {

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
    public ResponseEntity<UserResponse> modifyEmail(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody EmailModifyRequest req) {
        UserResponse res = userService.modifyEmail(principal.getId(), req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/password-change")
    public ResponseEntity<?> passwordChange(@AuthenticationPrincipal UserDetails principal, @RequestBody @Valid PasswordChangeRequest req) {
        String username = principal.getUsername();
        userService.passwordChange(username, req);
        return ResponseEntity.ok().build();
    }

}
