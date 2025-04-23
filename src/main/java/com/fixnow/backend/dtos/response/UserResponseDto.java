package com.fixnow.backend.dtos.response;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    // No password field here
} 