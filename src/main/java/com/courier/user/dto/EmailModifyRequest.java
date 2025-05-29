package com.courier.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailModifyRequest {

    @Email(message = "{email.format.invalid}")
    @NotBlank(message = "{email.not.blank}")
    private String email;
}
