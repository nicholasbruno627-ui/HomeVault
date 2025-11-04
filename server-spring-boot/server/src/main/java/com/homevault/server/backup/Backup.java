package com.homevault.server.backup;

import com.homevault.server.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity // directly map to database table
@Table(name = "backups", indexes = {
    @Index(name = "ix_backups_owner", columnList = "owner_id"), // index of backup owner
    @Index(name = "ix_backups_created", columnList = "createdAt") // index of when backups are created
})
public class Backup {

  public enum Status { STARTED, SUCCESS, FAILED }

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY) // only loaded when needed
  @JoinColumn(name = "owner_id", nullable = false) // a user can have more than one backup
  private User owner;

  //where archives live
  @Column(nullable = false, length = 1024)
  private String location;


  // size of backup
  @Column(nullable = false)
  private long sizeBytes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private Status status = Status.STARTED; // tracks when backup begins and completes

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column
  private Instant completedAt;

  public Backup() {}

  public Backup(User owner, String location, long sizeBytes) {
    this.owner = owner;
    this.location = location;
    this.sizeBytes = sizeBytes;
  }

  @PrePersist
  public void onCreate() {
    if (this.createdAt == null) this.createdAt = Instant.now();
  }

  // getters and setters
  public UUID getId() { return id; }

  public User getOwner() { return owner; }
  public void setOwner(User owner) { this.owner = owner; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public long getSizeBytes() { return sizeBytes; }
  public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

  public Instant getCreatedAt() { return createdAt; }

  public Instant getCompletedAt() { return completedAt; }
  public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
