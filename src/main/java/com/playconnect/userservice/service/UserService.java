package com.playconnect.userservice.service;

import com.playconnect.userservice.config.JWTService;
import com.playconnect.userservice.dto.user.CommonInfoUserResponse;
import com.playconnect.userservice.dto.user.UpdateUserRequest;
import com.playconnect.userservice.dto.user.UserRequest;
import com.playconnect.userservice.dto.user.UserResponse;
import com.playconnect.userservice.error.UnauthorizedAccessException;
import com.playconnect.userservice.model.Role;
import com.playconnect.userservice.model.User;
import com.playconnect.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JWTService jwtService;

    public void validateUser(Long requestUserId, String token) {
        // Extract JWT Token
        Long tokenUserId = jwtService.getUserIdFromToken(token);
        // Check if the User IDs match
        if (!tokenUserId.equals(requestUserId)) {
            throw new UnauthorizedAccessException("User not authorized to access this resource");
        }
    }

    public void createUser(UserRequest userRequest) {
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);
        log.info("User {} is saved", user.getUserId());
    }

    public List<UserResponse> getUsers() {
        List<User> events = userRepository.findAll();
        return events.stream().map(this::mapToUserResponse).toList();
    }

    public UserResponse getUserById(Long userId, String token) {
        // validate user
        validateUser(userId, token);
        // continue with the logic
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    public CommonInfoUserResponse getCommonInfoUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return CommonInfoUserResponse.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public UserResponse getUserByEmail(String email, String token) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        // validate user
        validateUser(user.getUserId(), token);
        // return the user
        return mapToUserResponse(user);
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest, String token) {
        // validate user
        validateUser(userId, token);
        // continue with the logic
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setEmail(updateUserRequest.getEmail());
        userRepository.save(user);
        return mapToUserResponse(user);
    }

    public void deleteUser(Long userId, String token) {
        // validate user
        validateUser(userId, token);
        // continue with the logic
        userRepository.deleteById(userId);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
