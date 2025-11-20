package com.homevault.client.backup;

import java.time.Instant;
import java.util.UUID;

public class BackupModel {

    private UUID id;
    private String location;
    private long sizeBytes;
    private String status;
    private Instant createdAt;

    public BackupModel(UUID id, String location, long sizeBytes, String status, Instant createdAt) {
        this.id = id;
        this.location = location;
        this.sizeBytes = sizeBytes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getLocation() { return location; }
    public long getSizeBytes() { return sizeBytes; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
