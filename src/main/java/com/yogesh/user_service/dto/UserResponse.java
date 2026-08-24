package com.yogesh.user_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
