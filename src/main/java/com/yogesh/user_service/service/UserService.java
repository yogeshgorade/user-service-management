package com.yogesh.user_service.service;

import com.yogesh.user_service.dto.CreateUserRequest;
import com.yogesh.user_service.dto.UpdateUserRequest;
import com.yogesh.user_service.dto.UserResponse;
import com.yogesh.user_service.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUserList();

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
    Page<UserResponse> getAllUserList(int pageNo, int pageSize,String sortBy,String direction);

    Page<UserResponse> searchUser(String firstName);
}
