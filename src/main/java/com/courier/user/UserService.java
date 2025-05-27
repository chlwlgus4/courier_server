package com.courier.user;

import com.courier.user.domain.User;
import com.courier.user.dto.UserResponse;
import com.courier.user.dto.UsernameCheckResponse;
import com.courier.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public UserResponse getUser(String username) {
        User user = repository.findByUsernameOrEmail(username, username).orElseThrow();
        return UserResponse.of(user);
    }

    public UsernameCheckResponse isUsernameAvailable(String username) {
        boolean isAvailable = !repository.existsByUsername(username);
        return new UsernameCheckResponse(isAvailable);
    }
}
