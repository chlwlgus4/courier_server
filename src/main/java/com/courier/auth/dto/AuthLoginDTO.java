package com.courier.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthLoginDTO {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;
}
