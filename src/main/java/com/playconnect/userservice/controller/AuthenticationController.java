package com.playconnect.userservice.controller;

import com.playconnect.userservice.dto.auth.AuthenticationRequest;
import com.playconnect.userservice.dto.auth.AuthenticationResponse;
import com.playconnect.userservice.dto.auth.RegistrationRequest;
import com.playconnect.userservice.dto.auth.UpdateUserPasswordRequest;
import com.playconnect.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updateUserPassword(@RequestBody UpdateUserPasswordRequest request) {
        authenticationService.updateUserPassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }
}
