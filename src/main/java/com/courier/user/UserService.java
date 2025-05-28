package com.courier.user;

import com.courier.handler.exception.BadRequestException;
import com.courier.handler.exception.ResourceNotFoundException;
import com.courier.user.domain.User;
import com.courier.user.dto.EmailModifyRequest;
import com.courier.user.dto.PasswordChangeRequest;
import com.courier.user.dto.UserResponse;
import com.courier.user.dto.UsernameCheckResponse;
import com.courier.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)"
                    + "(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$"
    );

    private final UserRepository userRepository;

    public UserResponse getUser(String username) {
        User user = repository.findByUsernameOrEmail(username, username).orElseThrow();
        return UserResponse.of(user);
    }

    public UsernameCheckResponse isUsernameAvailable(String username) {
        boolean isAvailable = !repository.existsByUsername(username);
        return new UsernameCheckResponse(isAvailable);
    }

    public UserResponse modifyEmail(Long id, EmailModifyRequest req) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        u.setEmail(req.getEmail());
        return UserResponse.of(userRepository.save(u));
    }

    @Transactional
    public void passwordChange(String username, PasswordChangeRequest dto) {
        User u = repository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(dto.getOldPassword(), u.getPassword())) {
            throw new BadRequestException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (!PASSWORD_PATTERN.matcher(dto.getNewPassword()).matches()) {
            throw new BadRequestException(
                    "새 비밀번호는 최소 8자, 대문자·소문자·숫자·특수문자 각 1개 이상 포함해야 합니다."
            );
        }

        u.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        repository.save(u);
    }

}
