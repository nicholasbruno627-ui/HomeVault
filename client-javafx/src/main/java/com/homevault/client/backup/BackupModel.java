package com.homevault.client.backup;

import java.time.Instant;
import java.util.UUID;

public class BackupModel {

    private UUID id;
    private UUID userId;
    private String location;
    private long sizeBytes;
    private String status;
    private Instant createdAt;

    private String encryptedData;
    private String iv;
    private String salt;

    public BackupModel() { }

    //getters and setters

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getEncryptedData() { return encryptedData; }
    public void setEncryptedData(String encryptedData) { this.encryptedData = encryptedData; }

    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    //creating backups *still  need to incorporate crypto*
    public static BackupModel createLocalBackup(UUID userId) {
        BackupModel m = new BackupModel();
        m.setUserId(userId);
        m.setLocation("local");
        m.setEncryptedData("placeholder");
        m.setIv("placeholder");
        m.setSalt("placeholder");
        return m;
    }
}
