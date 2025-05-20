package com.courier.auth.dto;

import com.courier.user.domain.User;

public record UserResponseDTO(
        Long id, String username, String name, String email, String phone, String role
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }

}
