package com.homevault.client.service;

import java.util.UUID;

public class AuthSession {

    private static AuthSession instance = new AuthSession();

    private UUID userId;
    private String token;

    private AuthSession() {}

    public static AuthSession getInstance() {
        return instance;
    }

    //getters

    public UUID getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    //setters

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //logs out the user on the client side, resets apps memory so user isn't authenticated anymore

    public void clear() {
        this.userId = null;
        this.token = null;
    }
}
