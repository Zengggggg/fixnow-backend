package com.fixnow.backend.controllers;

import com.fixnow.backend.dtos.request.LoginRequestDto;
import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.response.UserResponseDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.User;
import com.fixnow.backend.services.UserService;
import com.fixnow.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Base path for authentication related endpoints
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; // Needed for login check
    private final JwtUtil jwtUtil; // Inject JwtUtil

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            UserResponseDto registeredUser = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            // Handle cases like username/email already exists
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            // Use findByEmail and getEmail from DTO
            User user = userService.findByEmail(loginRequestDto.getEmail()); // Changed to findByEmail
            if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                // Generate JWT with username and userId
                String jwtToken = jwtUtil.generateToken(user.getUsername(), user.getId()); // Pass userId
                // Return token in response
                return ResponseEntity.ok(Map.of("token", jwtToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed due to an internal error.");
        }
    }

    // Note: User update might belong in a UserController, but placed here for simplicity
    // Needs proper security (e.g., check if authenticated user matches the ID)
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto updateDto) {
         // TODO: Add security check: Ensure the authenticated user is the one being updated or is an admin.
        try {
            UserResponseDto updatedUser = userService.updateUser(id, updateDto);
            return ResponseEntity.ok(updatedUser);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user.");
        }
    }
} 