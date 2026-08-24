package com.yogesh.user_service.controller;

import com.yogesh.user_service.dto.CreateUserRequest;
import com.yogesh.user_service.dto.UpdateUserRequest;
import com.yogesh.user_service.dto.UserResponse;
import com.yogesh.user_service.entity.User;
import com.yogesh.user_service.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(
        name = "User Management",
        description = "APIs for creating, retrieving, updating, searching and deleting users"
)
public class UserController {
   private final UserService userService;
   @Operation(
           summary = "Create a new user",
           description = "Creates a new user after validating the request and checking email and mobile uniqueness."
   )
   @ApiResponses({
           @ApiResponse(
                   responseCode = "201",
                   description = "User created successfully"
           ),
           @ApiResponse(
                   responseCode = "400",
                   description = "Invalid request data"
           ),
           @ApiResponse(
                   responseCode = "409",
                   description = "Email or mobile number already exists"
           )
   })
   @PostMapping
   public ResponseEntity<UserResponse> createUser(
           @Valid @RequestBody CreateUserRequest request) {
       log.info("Create User Controller started.....");
      UserResponse response = userService.createUser(request);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(response);
   }
   @Operation(
           summary = "Get user by ID",
           description = "Retrieves a single user using the unique user ID."
   )
   @ApiResponses({
           @ApiResponse(
                   responseCode = "200",
                   description = "User found successfully"
           ),
           @ApiResponse(
                   responseCode = "400",
                   description = "Invalid user ID"
           ),
           @ApiResponse(
                   responseCode = "404",
                   description = "User not found"
           )
   })
   @GetMapping("/{id}")
   public ResponseEntity<UserResponse> getUserById(
           @PathVariable Long id) {

      UserResponse response = userService.getUserById(id);

      return ResponseEntity.ok(response);
   }
   @Operation(
           summary = "Get users with pagination and sorting",
           description = "Retrieves users using page number, page size, sorting field and sorting direction."
   )
   @ApiResponses({
           @ApiResponse(
                   responseCode = "200",
                   description = "Users retrieved successfully"
           ),
           @ApiResponse(
                   responseCode = "400",
                   description = "Invalid pagination or sorting parameters"
           )
   })
   @GetMapping
   public ResponseEntity<Page<UserResponse>> getAllUsers(
           @RequestParam(defaultValue = "0") int pageNo,
           @RequestParam(defaultValue = "5") int pageSize,
           @RequestParam(defaultValue = "id") String sortBy,
           @RequestParam(defaultValue = "asc") String direction) {

      Page<UserResponse> responses =
              userService.getAllUserList(
                      pageNo,
                      pageSize,
                      sortBy,
                      direction
              );

      return ResponseEntity.ok(responses);
   }
   @Operation(
           summary = "Update user",
           description = "Updates the details of an existing user using the user ID."
   )
   @ApiResponses({
           @ApiResponse(
                   responseCode = "200",
                   description = "User updated successfully"
           ),
           @ApiResponse(
                   responseCode = "400",
                   description = "Invalid request data"
           ),
           @ApiResponse(
                   responseCode = "404",
                   description = "User not found"
           ),
           @ApiResponse(
                   responseCode = "409",
                   description = "Email or mobile number already exists"
           )
   })
   @PutMapping("/{id}")
   public ResponseEntity<UserResponse> updateUser(
           @PathVariable Long id,
           @Valid @RequestBody UpdateUserRequest request) {

      UserResponse response =
              userService.updateUser(id, request);
      return ResponseEntity.ok(response);
   }
   @Operation(
           summary = "Delete user",
           description = "Deletes an existing user using the user ID."
   )
   @ApiResponses({
           @ApiResponse(
                   responseCode = "204",
                   description = "User deleted successfully"
           ),
           @ApiResponse(
                   responseCode = "404",
                   description = "User not found"
           )
   })
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteUser(
           @PathVariable Long id) {

      userService.deleteUser(id);
      return ResponseEntity.noContent().build();
   }
  @Operation(
          summary = "Search users by first name",
          description = "Searches users whose first name contains the provided text, ignoring case."
  )
  @ApiResponses({
          @ApiResponse(
                  responseCode = "200",
                  description = "Users found successfully"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "Invalid search criteria"
          ),
          @ApiResponse(
                  responseCode = "404",
                  description = "No users found"
          )
  })
  @GetMapping("/search")
  public ResponseEntity<Page<UserResponse>> searchUser(
          @RequestParam String firstName) {

     Page<UserResponse> response =
             userService.searchUser(firstName);

     return ResponseEntity.ok(response);
  }
}

