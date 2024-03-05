package com.playconnect.userservice.service;

import com.playconnect.userservice.dto.UserRequest;
import com.playconnect.userservice.dto.UserResponse;
import com.playconnect.userservice.model.User;
import com.playconnect.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
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
                .email(userRequest.getEmail())
                .build();

        userRepository.save(user);
        log.info("User {} is saved", user.getUserId());
    }

    public List<UserResponse> getUsers() {
        List<User> events = userRepository.findAll();
        return events.stream().map(this::mapToUserResponse).toList();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .build();
    }
}
