package com.yogesh.user_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name",nullable = false, length = 100)
    private String firstName;
    @Column(name = "last_name",length = 100)
    private String lastName;
    @NotBlank
    @Email
    @Column(name = "email",unique = true,length = 255)
    private String  email;
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    @Column(name = "mobile",unique = true,nullable = false, length = 10)
    private String mobile;
    @Column(name = "status",nullable = false)
    private Boolean status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
