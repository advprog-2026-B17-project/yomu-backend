package id.ac.ui.cs.advprog.yomubackend.auth.controller;

import id.ac.ui.cs.advprog.yomubackend.auth.dto.GoogleAuthResponse;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.utils.JwtUtils;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/oauth")
public class GoogleOAuthController {

    @Value("${yomu.google.client-id}")
    private String googleClientId;

    @Value("${yomu.google.client-secret}")
    private String googleClientSecret;

    @Value("${yomu.google.redirect-uri:http://localhost:3000/api/auth/google/callback}")
    private String redirectUri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/google")
    public ResponseEntity<?> initiateGoogleOAuth() {
        String state = java.util.UUID.randomUUID().toString();

        String authUrl = UriComponentsBuilder
                .fromUriString(GOOGLE_AUTH_URL)
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .queryParam("state", state)
                .queryParam("access_type", "online")
                .build()
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authUrl))
                .build();
    }

    @GetMapping("/google/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code, @RequestParam(required = false) String state) {
        if (code == null || code.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Missing authorization code"));
        }

        try {
            // Exchange code for tokens
            Map<String, Object> tokenResponse = exchangeCodeForToken(code);
            String accessToken = (String) tokenResponse.get("access_token");

            if (accessToken == null || accessToken.isBlank()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to obtain access token from Google"));
            }

            // Get user info from Google
            Map<String, Object> googleUser = fetchGoogleUser(accessToken);
            String googleId = (String) googleUser.get("sub");
            String email = (String) googleUser.get("email");
            String displayName = (String) googleUser.get("name");

            if (googleId == null || email == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Invalid Google user data"));
            }

            // Find or create user
            User user = userRepository.findByGoogleId(googleId)
                    .orElseGet(() -> {
                        // Try to find by email if exists (non-Google user)
                        User existingByEmail = userRepository.findByEmail(email).orElse(null);
                        if (existingByEmail != null && existingByEmail.getGoogleId() == null) {
                            // Link existing account to Google
                            existingByEmail.setGoogleId(googleId);
                            return userRepository.save(existingByEmail);
                        }
                        // Create new user
                        User newUser = new User();
                        newUser.setGoogleId(googleId);
                        newUser.setEmail(email);
                        newUser.setDisplayName(displayName != null ? displayName : email.split("@")[0]);
                        newUser.setUsername(email.split("@")[0]);
                        newUser.setPassword(""); // No password for Google users
                        newUser.setRole("USER");
                        return userRepository.save(newUser);
                    });

            String jwt = jwtUtils.generateToken(user.getUsername());

            // Check if user was just created (within last 5 seconds)
            boolean isNewUser = user.getCreatedAt() != null &&
                    java.time.Duration.between(user.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5;

            return ResponseEntity.ok(new GoogleAuthResponse(jwt, isNewUser));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "OAuth authentication failed: " + e.getMessage()));
        }
    }

    private Map<String, Object> exchangeCodeForToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        RequestEntity<Map<String, String>> request = RequestEntity
                .post(URI.create(GOOGLE_TOKEN_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params);

        ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Empty response from Google token endpoint");
        }
        return body;
    }

    private Map<String, Object> fetchGoogleUser(String accessToken) {
        RequestEntity<?> request = RequestEntity
                .get(URI.create(GOOGLE_USERINFO_URL))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Empty response from Google userinfo endpoint");
        }
        return body;
    }
}