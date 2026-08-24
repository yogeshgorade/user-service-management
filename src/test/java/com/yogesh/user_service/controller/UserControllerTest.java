package com.yogesh.user_service.controller;

import com.yogesh.user_service.dto.CreateUserRequest;
import com.yogesh.user_service.dto.UserResponse;
import com.yogesh.user_service.exception.ResourceAlreadyExistsException;
import com.yogesh.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    @Test
    void createUser_shouldReturn201_whenRequestIsValid() throws Exception {

        // Arrange
        UserResponse response = new UserResponse();

        response.setId(1L);
        response.setFirstName("Yogesh");
        response.setLastName("Gorade");
        response.setEmail("yogesh@gmail.com");
        response.setMobile("9876543210");
        response.setStatus(true);

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "firstName": "Yogesh",
                            "lastName": "Gorade",
                            "email": "yogesh@gmail.com",
                            "mobile": "9876543210"
                        }
                        """)
                )
                .andExpect(status().isCreated());
    }
    @Test
    void createUser_shouldReturn400_whenRequestIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "firstName": "",
                            "lastName": "Gorade",
                            "email": "wrong-email",
                            "mobile": "123"
                        }
                        """)
                )
                .andExpect(status().isBadRequest());

        verify(
                userService,
                never()
        ).createUser(any(CreateUserRequest.class));
    }
    @Test
    void createUser_shouldReturn409_whenEmailAlreadyExists() throws Exception {

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(
                        new ResourceAlreadyExistsException(
                                "Email already exists."
                        )
                );

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "firstName": "Yogesh",
                            "lastName": "Gorade",
                            "email": "yogesh@gmail.com",
                            "mobile": "9876543210"
                        }
                        """)
                )
                .andExpect(status().isConflict());
    }
}
