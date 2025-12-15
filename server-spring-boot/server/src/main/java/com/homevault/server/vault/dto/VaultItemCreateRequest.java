package com.homevault.server.vault.dto;

public class VaultItemCreateRequest {

    private String title;
    private String username;
    private String secret; 

    public VaultItemCreateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getSecret() {
        return secret;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
