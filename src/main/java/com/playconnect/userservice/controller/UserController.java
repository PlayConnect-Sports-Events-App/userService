package com.playconnect.userservice.controller;

import com.playconnect.userservice.dto.user.CommonInfoUserResponse;
import com.playconnect.userservice.dto.user.UpdateUserRequest;
import com.playconnect.userservice.dto.user.UserRequest;
import com.playconnect.userservice.dto.user.UserResponse;
import com.playconnect.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Todo: need jwt token to access this controller, when updating or deleting a user need to compare the jwt token if it is the same email as the user
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
    public UserResponse getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/common")
    public CommonInfoUserResponse getCommonInfoUserById(@PathVariable Long userId) {
        return userService.getCommonInfoUserById(userId);
    }

    @GetMapping("/email/{email}")
    public UserResponse getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest userRequest) {
        return userService.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "User deleted successfully";
    }
}
