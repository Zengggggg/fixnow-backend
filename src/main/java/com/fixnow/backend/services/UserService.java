package com.fixnow.backend.services;

import com.fixnow.backend.dtos.request.ChangePasswordDto;
import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.response.UserResponseDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.User;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface UserService {
    UserResponseDto registerUser(UserRegistrationDto registrationDto, HttpServletRequest request) throws GeneralSecurityException, IOException;
    User findByUsername(String username); // May be needed for authentication
    User findByEmail(String email);
    //UserResponseDto updateUser(Long id, UserUpdateDto updateDto); // Needs authentication context later
    User findOrCreateGoogleUser(String email, String name);
//    String loginWithGoogle(String idToken) throws GeneralSecurityException, IOException;

    User findById(Long id);
    void updatePassword(String email, String rawPassword);
    boolean existsByEmail(String email);
    boolean patchField(String currentUsername, String field, String value);
    void changePassword(String username, ChangePasswordDto dto);

}