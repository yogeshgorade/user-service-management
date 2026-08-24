package com.yogesh.user_service;
import com.yogesh.user_service.dto.CreateUserRequest;
import com.yogesh.user_service.dto.UpdateUserRequest;
import com.yogesh.user_service.dto.UserResponse;
import com.yogesh.user_service.entity.User;
import com.yogesh.user_service.exception.ResourceAlreadyExistsException;
import com.yogesh.user_service.exception.ResourceNotFoundException;
import com.yogesh.user_service.repository.UserRepository;
import com.yogesh.user_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldCreateUserSuccessfully() {

        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setFirstName("Yogesh");
        request.setLastName("Gorade");
        request.setEmail("yogesh@gmail.com");
        request.setMobile("9876543210");

        when(userRepository.existsByEmail("yogesh@gmail.com"))
                .thenReturn(false);

        when(userRepository.existsByMobile("9876543210"))
                .thenReturn(false);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFirstName("Yogesh");
        savedUser.setLastName("Gorade");
        savedUser.setEmail("yogesh@gmail.com");
        savedUser.setMobile("9876543210");
        savedUser.setStatus(true);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("Yogesh", response.getFirstName());
        assertEquals("Gorade", response.getLastName());
        assertEquals("yogesh@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getMobile());
        assertTrue(response.getStatus());
    }
    @Test
    void createUser_shouldThrowException_whenEmailAlreadyExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setFirstName("Yogesh");
        request.setEmail("yogesh@gmail.com");
        request.setMobile("9876543210");
        when(userRepository.existsByEmail("yogesh@gmail.com"))
                .thenReturn(true);
        // Act + Assert
        ResourceAlreadyExistsException exception =
                assertThrows(
                        ResourceAlreadyExistsException.class,
                        () -> userService.createUser(request)
                );

        assertEquals(
                "Email already exists.",
                exception.getMessage()
        );

        verify(
                userRepository,
                never()
        ).save(any(User.class));
    }
    @Test
    void createUser_shouldThrowException_whenMobileAlreadyExists() {

        // Arrange
        CreateUserRequest request = new CreateUserRequest();

        request.setFirstName("Yogesh");
        request.setEmail("yogesh@gmail.com");
        request.setMobile("9876543210");

        when(userRepository.existsByEmail("yogesh@gmail.com"))
                .thenReturn(false);

        when(userRepository.existsByMobile("9876543210"))
                .thenReturn(true);

        // Act + Assert
        ResourceAlreadyExistsException exception =
                assertThrows(
                        ResourceAlreadyExistsException.class,
                        () -> userService.createUser(request)
                );

        // Assert exception message
        assertEquals(
                "Mobile number already exists.",
                exception.getMessage()
        );

        // Verify user was not saved
        verify(
                userRepository,
                never()
        ).save(any(User.class));
    }
    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Yogesh");
        user.setLastName("Gorade");
        user.setEmail("yogesh@gmail.com");
        user.setMobile("9876543210");
        user.setStatus(true);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        UserResponse response =
                userService.getUserById(userId);
        // Assert
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("Yogesh", response.getFirstName());
        assertEquals("Gorade", response.getLastName());
        assertEquals("yogesh@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getMobile());
        assertTrue(response.getStatus());
        verify(userRepository).findById(userId);
    }
    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {

        // Arrange
        Long userId = 99L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> userService.getUserById(userId)
                );

        assertEquals(
                "User not found with id: " + userId,
                exception.getMessage()
        );

        verify(userRepository).findById(userId);
    }
    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() {

        // Arrange
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("Yogesh");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }
    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {

        // Arrange
        Long userId = 99L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> userService.deleteUser(userId)
                );

        assertEquals(
                "User not found with id: " + userId,
                exception.getMessage()
        );

        verify(userRepository).findById(userId);

        verify(userRepository, never())
                .delete(any(User.class));
    }
    @Test
    void updateUser_shouldUpdateUserSuccessfully() {

        // Arrange
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("OldName");
        existingUser.setLastName("OldLastName");
        existingUser.setEmail("old@gmail.com");
        existingUser.setMobile("9876543210");
        existingUser.setStatus(true);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Yogesh");
        request.setLastName("Gorade");
        request.setEmail("old@gmail.com");
        request.setMobile("9876543210");
        request.setStatus(true);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenReturn(existingUser);

        // Act
        UserResponse response =
                userService.updateUser(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals("Yogesh", response.getFirstName());
        assertEquals("Gorade", response.getLastName());

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }
}