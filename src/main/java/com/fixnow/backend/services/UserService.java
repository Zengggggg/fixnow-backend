package com.fixnow.backend.services;

import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.response.UserResponseDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.User;

public interface UserService {
    UserResponseDto registerUser(UserRegistrationDto registrationDto);
    User findByUsername(String username); // May be needed for authentication
    User findByEmail(String email);
    UserResponseDto updateUser(Long id, UserUpdateDto updateDto); // Needs authentication context later
} 