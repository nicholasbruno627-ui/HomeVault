package com.homevault.server.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(201).body(response);

        } catch (IllegalArgumentException ex) {

            if (ex.getMessage().toLowerCase().contains("email")) {
                return ResponseEntity
                        .status(409)
                        .body(Map.of("error", "Email already exists"));
            }

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Unexpected server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", ex.getMessage());
            return ResponseEntity.status(401).body(body);
        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Unexpected server error");
            body.put("details", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return ResponseEntity.status(500).body(body);
        }
    }
}
