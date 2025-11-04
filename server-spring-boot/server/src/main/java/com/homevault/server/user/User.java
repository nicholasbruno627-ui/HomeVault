package com.homevault.server.user;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity //directly map to database table
@Table(name = "users", indexes = {
    @Index(name = "ix_users_email_unique", columnList = "email", unique = true)
})
public class User {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false, unique = true, length = 320)
  private String email;

  @Column(nullable = false, length = 100)
  private String displayName;

  //stores argon2 hash, not actual password
  @Column(nullable = false, length = 255)
  private String passwordHash;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  public User() {}

  public User(String email, String displayName, String passwordHash) { //a user has an email, their display name, and a hashed password
    this.email = email;
    this.displayName = displayName;
    this.passwordHash = passwordHash;
  }

  @PrePersist
  public void onCreate() {
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate //runs automatically before the entity is updated
  public void onUpdate() {
    this.updatedAt = Instant.now(); //updates to current time
  }

  //getters and setters
  public UUID getId() { return id; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getDisplayName() { return displayName; }
  public void setDisplayName(String displayName) { this.displayName = displayName; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
