package com.fixnow.backend.dtos.request;

import lombok.Data;

@Data // Lombok annotation for getters, setters, toString, equals, hashCode
public class UserRegistrationDto {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String email;
} 