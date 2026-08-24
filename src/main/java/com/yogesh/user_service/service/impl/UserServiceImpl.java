package com.yogesh.user_service.service.impl;

import com.yogesh.user_service.dto.CreateUserRequest;
import com.yogesh.user_service.dto.UpdateUserRequest;
import com.yogesh.user_service.dto.UserResponse;
import com.yogesh.user_service.entity.User;
import com.yogesh.user_service.exception.InvalidPaginationException;
import com.yogesh.user_service.exception.InvalidSearchException;
import com.yogesh.user_service.exception.ResourceAlreadyExistsException;
import com.yogesh.user_service.exception.ResourceNotFoundException;
import com.yogesh.user_service.repository.UserRepository;
import com.yogesh.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserResponse createUser(CreateUserRequest request) {
        log.info("creating user with email:{}",request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("User creation failed. Email already exists: {}",
                    request.getEmail());
            throw new ResourceAlreadyExistsException(
                    "Email already exists."
            );
        }
        if (userRepository.existsByMobile(request.getMobile())) {
            log.warn("User creation failed. Mobile already exists.");
            throw new ResourceAlreadyExistsException(
                    "Mobile number already exists."
            );
        }
        User user = convertToUserBean(request);
        User responseUser = userRepository.save(user);
        log.info("User created successfully with ID: {}",
                user.getId());
        UserResponse response=convertToUserResponse(responseUser);
        return response;
    }
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
        return convertToUserResponseBean(user);
    }
    @Override
    public List<UserResponse> getAllUserList() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No user data found.");
        }
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(convertToUserResponseBean(user));
        }
        return userResponses;
    }

    @Override
    public UserResponse updateUser(
            Long id,
            UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id));
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(
                request.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email already exists."
            );
        }
        if (!user.getMobile().equals(request.getMobile())
                && userRepository.existsByMobile(
                request.getMobile())) {
            throw new ResourceAlreadyExistsException(
                    "Mobile number already exists."
            );
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setStatus(request.getStatus());
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return convertToUserResponseBean(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));

        userRepository.delete(user);
    }

    @Override
    public Page<UserResponse> getAllUserList(int pageNo, int pageSize,String sortBy,String direction) {
        Sort sort;
        if (pageNo <0){
            throw new InvalidPaginationException("Page number cannot be negative. ");
        }
        if (pageSize <1 || pageSize >100){
            throw new InvalidPaginationException("Page size must be between 1 to 100. ");
        }
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new InvalidPaginationException("Direction must be asc or desc. ");
        }
        Set<String> allowedSortFields =
                Set.of(
                        "id",
                        "firstName",
                        "lastName",
                        "email",
                        "createdAt",
                        "updatedAt"
                );
        if (!allowedSortFields.contains(sortBy)) {
            throw new InvalidPaginationException(
                    "Invalid sort field: " + sortBy
            );
        }
        if (direction.equalsIgnoreCase("desc")){
            sort=Sort.by(sortBy).descending();
        }else {
            sort=Sort.by(sortBy).ascending();
        }
        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);
        Page<User>users=userRepository.findAll(pageable);
        return users.map(this::convertToUserResponse);
    }

    @Override
    public Page<UserResponse> searchUser(String search) {
        if (search == null || search.isEmpty()) {
            throw new InvalidSearchException(
                    "First name cannot be empty."
            );
        }
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> users =
                userRepository.searchUsers(search, pageable);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No users found for first name: " + search
            );
        }
        return users.map(this::convertToUserResponse);
    }

    private UserResponse convertToUserResponseBean(User user) {
        UserResponse response=new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setMobile(user.getMobile());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    private UserResponse convertToUserResponse(User responseUser) {
        UserResponse saveUser=new UserResponse();
        saveUser.setFirstName(responseUser.getFirstName());
        saveUser.setLastName(responseUser.getLastName());
        saveUser.setEmail(responseUser.getEmail());
        saveUser.setMobile(responseUser.getMobile());
        saveUser.setCreatedAt(responseUser.getCreatedAt());
        saveUser.setUpdatedAt(responseUser.getUpdatedAt());
        saveUser.setStatus(responseUser.getStatus());
        return saveUser;
    }

    private User convertToUserBean(CreateUserRequest request) {
        User response=new User();
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setEmail(request.getEmail());
        response.setStatus(true);
        response.setMobile(request.getMobile());
        LocalDateTime now=LocalDateTime.now();
        response.setCreatedAt(now);
        response.setUpdatedAt(now);
        return response;
    }
}
