package com.fixnow.backend.services.impl;

import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.response.UserResponseDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.User;
import com.fixnow.backend.mun.Provider;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fixnow.backend.util.JwtUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok constructor injection for final fields
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional // Ensure atomicity
    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        // Encode the password before saving
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        User savedUser = userRepository.save(newUser);

        return mapUserToResponseDto(savedUser);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        // Consider returning Optional<User> or handling UserNotFoundException specifically
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto updateDto) {
        // Authentication/Authorization check should happen here in a real app
        // For now, we just find the user by ID
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Update email if provided and different
        if (updateDto.getEmail() != null && !updateDto.getEmail().isEmpty() && !updateDto.getEmail().equals(existingUser.getEmail())) {
             // Check if the new email is already taken by another user
            if (userRepository.existsByEmail(updateDto.getEmail())) {
                 throw new IllegalArgumentException("Email already exists");
            }
            existingUser.setEmail(updateDto.getEmail());
        }

        // Update password if provided
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            // In a real app, you might want to verify the user's current password first
            existingUser.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return mapUserToResponseDto(updatedUser);
    }
    @Override
    public User findOrCreateGoogleUser(String email, String name){
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        User newUser = new User();
        newUser.setUsername(name);
        newUser.setEmail(email);
        newUser.setProvider(Provider.GOOGLE);
        return userRepository.save(newUser);
    }

    @Override
    public String loginWithGoogle(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = verifyToken(idToken);

        assert payload != null;
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String providerId = payload.getSubject(); // This is Google's unique user ID

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        new User(
                                null,                    // id (auto-generated)
                                name,                    // username
                                "",                      // password (not needed for OAuth)
                                email,                   // email
                                Provider.GOOGLE,         // provider
                                providerId               // providerId
                        )
                ));

        return jwtUtil.generateToken(user.getUsername(), user.getId());
    }
    private GoogleIdToken.Payload verifyToken(String idTokenString) {
        try {
            //    @Value("${google.client.id}")
            String clientId = "424612164457-pmskbghvkdihh8lcs63odsb17bhukfs5.apps.googleusercontent.com";
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                System.out.println("Invalid ID token.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to map User entity to UserResponseDto
    private UserResponseDto mapUserToResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
} 