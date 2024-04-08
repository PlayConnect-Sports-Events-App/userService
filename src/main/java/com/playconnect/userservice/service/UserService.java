package com.playconnect.userservice.service;

import com.playconnect.userservice.dto.user.CommonInfoUserResponse;
import com.playconnect.userservice.dto.user.UpdateUserRequest;
import com.playconnect.userservice.dto.user.UserRequest;
import com.playconnect.userservice.dto.user.UserResponse;
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

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return mapToUserResponse(user);
    }

    public CommonInfoUserResponse getCommonInfoUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return CommonInfoUserResponse.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return mapToUserResponse(user);
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setEmail(updateUserRequest.getEmail());
        userRepository.save(user);
        return mapToUserResponse(user);
    }

    public void deleteUser(Long userId) {
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
