package com.courier.auth.dto;

import com.courier.user.domain.User;

public record AuthResponse(User user, String accessToken) {
}
