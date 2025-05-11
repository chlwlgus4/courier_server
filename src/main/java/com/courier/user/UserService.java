package com.courier.user;

import com.courier.user.domain.User;
import com.courier.user.dto.UserLoginDTO;
import com.courier.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public User saveUser(UserLoginDTO req) {

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role("ROLE_USER")
                .build();

        return userRepository.save(user);
    }
}
