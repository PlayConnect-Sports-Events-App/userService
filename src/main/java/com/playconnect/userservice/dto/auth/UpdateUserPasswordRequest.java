package com.playconnect.userservice.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserPasswordRequest {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
