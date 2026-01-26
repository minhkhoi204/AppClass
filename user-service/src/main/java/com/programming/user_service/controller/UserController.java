package com.programming.user_service.controller;

import com.programming.user_service.domain.dto.request.UserRequestDto;
import com.programming.user_service.domain.dto.response.UserResponseDto;
import com.programming.common.response.ApiResponse;
import com.programming.user_service.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // check existed user
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
        return ResponseEntity.ok(userService.userExists(id));
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    // get multiple users by ids
    @GetMapping("/batch")
    public ResponseEntity<List<UserResponseDto>> getUsersByIds(@RequestParam("ids") Set<Long> ids) {
        List<UserResponseDto> users = userService.getUsersByIds(ids);
        return ResponseEntity.ok(users);
    }

    // create user
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserRequestDto req) {
        UserResponseDto response = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("User created successfully", response));
    }

    // update user
    @PutMapping("/{id}/update")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto dto
    ) {
        UserResponseDto updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse("User deleted succcessfully", null));
    }

}