package com.homevault.client.service;

import java.util.UUID;


public class AuthSession {

    private static AuthSession instance;

    private UUID userId;
    private String email;

    private AuthSession() {}

    public static AuthSession getInstance() {
        if (instance == null) {
            instance = new AuthSession();
        }
        return instance;
    }

   
    public void login(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
    }

   
    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

   
    public void logout() {
        this.userId = null;
        this.email = null;
    }
}
