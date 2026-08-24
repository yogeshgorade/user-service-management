package com.yogesh.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CreateUserRequest {
    private long userId;
    @NotBlank
    @Size(max = 100)
    private String firstName;
    @Size(max=100)
    private String lastName;
    @Email
    @Size(max = 255)
    @NotBlank
    private String email;
    @Pattern(regexp = "^[0-9]{10}$")
    @NotBlank
    private String mobile;
}
