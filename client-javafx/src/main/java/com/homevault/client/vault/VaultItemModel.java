package com.homevault.client.vault;

import java.util.UUID;

public class VaultItemModel {
    private UUID id;
    private String title;
    private String username;
    private String secret;

    public VaultItemModel(UUID id, String title, String username, String secret) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.secret = secret;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getUsername() { return username; }
    public String getSecret() { return secret; }
}
