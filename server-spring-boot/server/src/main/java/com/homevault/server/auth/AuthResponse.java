package com.homevault.server.auth;

import java.util.UUID;

public class AuthResponse {
    private UUID userId;
    private String email;
    private String displayName;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
