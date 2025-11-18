package com.homevault.server.auth;

import java.util.UUID;

public class AuthResponse {

    private UUID userId;
    private String email;
    private String name;
    private String token;

    public AuthResponse(UUID userId, String email, String name, String token) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
