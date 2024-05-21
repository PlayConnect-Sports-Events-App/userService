package com.playconnect.userservice.controller;

import com.playconnect.userservice.config.JWTService;
import com.playconnect.userservice.dto.auth.AuthenticationRequest;
import com.playconnect.userservice.dto.auth.AuthenticationResponse;
import com.playconnect.userservice.dto.auth.RegistrationRequest;
import com.playconnect.userservice.dto.auth.UpdateUserPasswordRequest;
import com.playconnect.userservice.error.AuthenticationFailureException;
import com.playconnect.userservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updateUserPassword(@RequestBody UpdateUserPasswordRequest request, @RequestHeader("Authorization") String authHeader) {
        authenticationService.updateUserPassword(request, authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok("Password updated successfully");
    }
}

