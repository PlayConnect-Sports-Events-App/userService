package com.playconnect.userservice.dto.user;

import com.playconnect.userservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonInfoUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
