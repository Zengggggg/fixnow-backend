package com.fixnow.backend.services.impl;

import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.response.UserResponseDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.User;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Lombok constructor injection for final fields
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    // Helper method to map User entity to UserResponseDto
    private UserResponseDto mapUserToResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
} 