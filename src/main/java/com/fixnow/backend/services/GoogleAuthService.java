package com.fixnow.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.backend.models.User;
import com.fixnow.backend.mun.Provider;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.fixnow.backend.dtos.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class GoogleAuthService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    public AuthenticationResponse authenticate(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("424612164457-pmskbghvkdihh8lcs63odsb17bhukfs5.apps.googleusercontent.com"))
                .build();

        try {
            GoogleIdToken token = verifier.verify(idToken);
            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");

                // ✅ Kiểm tra xem user đã tồn tại chưa
                Optional<User> userOpt = userRepository.findByEmail(email);
                User user;

                if (userOpt.isPresent()) {
                    user = userOpt.get();
                } else {
                    // ✅ Tạo user mới
                    user = new User();
                    user.setEmail(email);
                    user.setUsername(name);
                    user.setPassword(null);
                    user.setProvider(Provider.GOOGLE); // Có thể dùng enum nếu muốn

                    // Có thể set default role ở đây nếu dùng Spring Security
                    // user.setRoles(Set.of(defaultRole));

                    user = userRepository.save(user);
                }

                // ✅ Tạo JWT token
                String jwt = jwtUtil.generateToken(user.getEmail(), user.getId());

                return new AuthenticationResponse(jwt);
            } else {
                throw new RuntimeException("Invalid ID token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed", e);
        }
    }
    // Extract the name from the Google token or use Google User Info API to get details
    private String extractUsernameFromGoogleToken(String googleToken) {
        // Call the Google User Info API to get details about the user
        String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        // Create the headers and set the authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + googleToken);

        // Make the request to the Google API to get user information
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                googleUserInfoUrl,
                HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers),
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            // Parse the response body to get the user's name
            String responseBody = response.getBody();
            // Extract the user's name from the JSON response (e.g., using a JSON parser like Jackson)

            return parseNameFromJson(responseBody);
        } else {
            throw new RuntimeException("Unable to retrieve user info from Google");
        }
    }

    private String parseNameFromJson(String json) {
        // Use a JSON parser (e.g., Jackson) to extract the 'name' field from the response
        // Example: Assume the response looks like: { "name": "John Doe", "email": "john.doe@gmail.com", ... }

        // For demonstration purposes, we will assume the name is the first field in the response
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(json);
            return jsonNode.get("name").asText();  // Extracts "name" field
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Google user info", e);
        }
    }


}
