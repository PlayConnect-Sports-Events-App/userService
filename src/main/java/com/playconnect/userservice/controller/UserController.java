package com.playconnect.userservice.controller;

import com.playconnect.userservice.dto.UserRequest;
import com.playconnect.userservice.dto.UserResponse;
import com.playconnect.userservice.model.User;
import com.playconnect.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
