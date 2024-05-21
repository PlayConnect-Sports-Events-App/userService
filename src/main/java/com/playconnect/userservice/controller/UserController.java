package com.playconnect.userservice.controller;

import com.playconnect.userservice.config.JWTService;
import com.playconnect.userservice.dto.user.CommonInfoUserResponse;
import com.playconnect.userservice.dto.user.UpdateUserRequest;
import com.playconnect.userservice.dto.user.UserRequest;
import com.playconnect.userservice.dto.user.UserResponse;
import com.playconnect.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Todo: need jwt token to access this controller, when updating or deleting a user need to compare the jwt token if it is the same id as the user
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public String createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return "User created successfully";
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.getUserById(userId, authHeader.replace("Bearer ", "")));
    }

    @GetMapping("/{userId}/common")
    public CommonInfoUserResponse getCommonInfoUserById(@PathVariable Long userId) {
        return userService.getCommonInfoUserById(userId);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.getUserByEmail(email, authHeader.replace("Bearer ", "")));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest userRequest, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.updateUser(userId, userRequest, authHeader.replace("Bearer ", "")));
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestHeader("Authorization") String authHeader) {
        userService.deleteUser(userId, authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok("User deleted successfully");
    }
}
