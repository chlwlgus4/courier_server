package com.courier.user;

import com.courier.user.dto.UserResponse;
import com.courier.user.dto.UsernameCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
