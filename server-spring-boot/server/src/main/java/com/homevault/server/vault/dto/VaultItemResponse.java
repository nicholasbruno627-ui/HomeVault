package com.homevault.server.vault.dto;

import java.time.Instant;
import java.util.UUID;

public class VaultItemResponse {

    private UUID id;
    private String title;
    private String username;
    private Instant createdAt;
    private Instant updatedAt;

    public VaultItemResponse(UUID id, String title, String username,
                             Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
