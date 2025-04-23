package com.fixnow.backend.dtos.request;

import lombok.Data;

@Data
public class UserUpdateDto {
    // We might not want to allow username changes easily.
    private String email; // Allow email update
    private String password; // Allow password update (provide current password for verification in service)
} 