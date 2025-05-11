package com.courier.user.dto;

import com.courier.user.domain.User;

public record UserResponse(User user, String token) {
}
