package com.fixnow.backend.services.impl;

import com.fixnow.backend.dtos.request.ChangePasswordDto;
import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.response.UserResponseDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.Role;
import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.models.VerificationToken;
import com.fixnow.backend.mun.Provider;
import com.fixnow.backend.repositories.RoleRepository;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.repositories.UserWalletRepository;
import com.fixnow.backend.repositories.VerificationTokenRepository;
import com.fixnow.backend.services.EmailVerifyService;
import com.fixnow.backend.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fixnow.backend.util.JwtUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Lombok constructor injection for final fields
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailVerifyService emailVerifyService;
    private final UserWalletRepository userWalletRepository;

    @Override
    public UserResponseDto registerUser(UserRegistrationDto registrationDto, HttpServletRequest request) {
        // Check if username or email already exists
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role freemiumRole = roleRepository.findByName("ROLE_FREE")
                .orElseThrow(() -> new IllegalStateException("Default role ROLE_FREE not found"));;
        User newUser = new User();
        UserWallet wallet = new UserWallet();

        newUser.setUsername(registrationDto.getUsername());
        newUser.setFirstName(registrationDto.getFirstName());
        newUser.setLastName(registrationDto.getLastName());
        newUser.setEmail(registrationDto.getEmail());
        // Encode the password before saving
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setProvider(Provider.LOCAL);
        newUser.setRole(freemiumRole);
        newUser.setEnabled(false);


        User savedUser = userRepository.save(newUser);


        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken();
        vt.setUser(savedUser);
        vt.setToken(token);
        vt.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationTokenRepository.save(vt);

        emailVerifyService.sendVerificationEmail(savedUser,token,request);

        return mapUserToResponseDto(savedUser);
    }

    @Override
    public void updatePassword(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).get();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void changePassword(String username, ChangePasswordDto dto) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean patchField(String currentUsername, String field, String value) {
        Optional<User> optionalUser = userRepository.findByEmail(currentUsername);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        switch (field) {
            case "username":
                user.setUsername(value);
                break;
            case "firstName":
                user.setFirstName(value);
                break;
            case "lastName":
                user.setLastName(value);
                break;
            case "discountRate":
                try {
                    user.setDiscountRate(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            // nếu muốn hỗ trợ update enabled, provider, v.v. thì thêm tiếp
            default:
                return false; // field không hợp lệ
        }

        userRepository.save(user);
        return true;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
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

//    @Override
//    @Transactional
//    public UserResponseDto updateUser(Long id, UserUpdateDto updateDto) {
//        // Authentication/Authorization check should happen here in a real app
//        // For now, we just find the user by ID
//        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
//
//        // Update email if provided and different
//        if (updateDto.getEmail() != null && !updateDto.getEmail().isEmpty() && !updateDto.getEmail().equals(existingUser.getEmail())) {
//             // Check if the new email is already taken by another user
//            if (userRepository.existsByEmail(updateDto.getEmail())) {
//                 throw new IllegalArgumentException("Email already exists");
//            }
//            existingUser.setEmail(updateDto.getEmail());
//        }
//
//        // Update password if provided
//        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
//            // In a real app, you might want to verify the user's current password first
//            existingUser.setPassword(passwordEncoder.encode(updateDto.getPassword()));
//        }
//
//        User updatedUser = userRepository.save(existingUser);
//        return mapUserToResponseDto(updatedUser);
//    }
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

//    @Override
//    public String loginWithGoogle(String idToken) throws GeneralSecurityException, IOException {
//        GoogleIdToken.Payload payload = verifyToken(idToken);
//        Role freemiumRole = roleRepository.findByName("FREEMIUM_USER")
//                .orElseThrow(() -> new IllegalStateException("Default role ROLE_FREE not found"));;
//        assert payload != null;
//        String email = payload.getEmail();
//        String name = (String) payload.get("name");
//        String providerId = payload.getSubject(); // This is Google's unique user ID
//
//        User user = userRepository.findByEmail(email)
//                .orElseGet(() -> userRepository.save(
//                        new User(
//                                null,                    // id (auto-generated)
//                                name,                    // username
//                                "",                      // password (not needed for OAuth)
//                                email,                   // email
//                                Provider.GOOGLE,         // provider
//                                providerId,               // providerId
//                                freemiumRole,
//                                null
//                        )
//                ));
//
//        return jwtUtil.generateToken(user.getUsername(), user.getId());
//    }
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