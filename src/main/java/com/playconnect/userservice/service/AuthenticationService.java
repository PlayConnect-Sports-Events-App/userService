package com.playconnect.userservice.service;

import com.playconnect.userservice.config.JWTService;
import com.playconnect.userservice.dto.auth.AuthenticationRequest;
import com.playconnect.userservice.dto.auth.AuthenticationResponse;
import com.playconnect.userservice.dto.auth.RegistrationRequest;
import com.playconnect.userservice.dto.auth.UpdateUserPasswordRequest;
import com.playconnect.userservice.error.AuthenticationFailureException;
import com.playconnect.userservice.error.InvalidPasswordException;
import com.playconnect.userservice.error.UnauthorizedAccessException;
import com.playconnect.userservice.model.Role;
import com.playconnect.userservice.model.User;
import com.playconnect.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public void validateUser(Long requestUserId, String token) {
        // Extract JWT Token
        Long tokenUserId = jwtService.getUserIdFromToken(token);
        // Check if the User IDs match
        if (!tokenUserId.equals(requestUserId)) {
            throw new UnauthorizedAccessException("User not authorized to access this resource");
        }
    }

    public AuthenticationResponse register(RegistrationRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));
            var token = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new AuthenticationFailureException("Invalid email or password");
        }
    }

    public void updateUserPassword(UpdateUserPasswordRequest request, String token){
        //validate user
        validateUser(request.getUserId(), token);
        //continue with the logic
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        }
        else
        {
            throw new InvalidPasswordException("Incorrect current password provided");
        }
    }
}
