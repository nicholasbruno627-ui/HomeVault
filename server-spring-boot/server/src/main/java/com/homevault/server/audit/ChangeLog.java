package com.homevault.server.audit;

import com.homevault.server.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity //directly map to database table
@Table(name = "change_logs", indexes = {
    @Index(name = "ix_change_logs_user", columnList = "user_id"),
    @Index(name = "ix_change_logs_entity", columnList = "entityType,entityId"),
    @Index(name = "ix_change_logs_time", columnList = "changedAt")
})
public class ChangeLog {

  public enum ChangeType { CREATE, UPDATE, DELETE, BACKUP, RESTORE, LOGIN, LOGOUT }

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY) //only loaded when needed
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ChangeType type;

  @Column(nullable = false, length = 50)
  private String entityType;   //vault item, user, backup

  @Column(nullable = false)
  private UUID entityId;

  @Column(nullable = false)
  private Instant changedAt;

  @Column(length = 1000)
  private String description;  //description of logs to see changes. *****still in work*****

  public ChangeLog() {}

  public ChangeLog(User user, ChangeType type, String entityType, UUID entityId, String description) {
    this.user = user;
    this.type = type;
    this.entityType = entityType;
    this.entityId = entityId;
    this.description = description;
  } //constructor for creating new log entries

  @PrePersist
  public void onCreate() {
    if (this.changedAt == null) this.changedAt = Instant.now();
  }
//getters and setters
  public UUID getId() { return id; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public ChangeType getType() { return type; }
  public void setType(ChangeType type) { this.type = type; }
  public String getEntityType() { return entityType; }
  public void setEntityType(String entityType) { this.entityType = entityType; }
  public UUID getEntityId() { return entityId; }
  public void setEntityId(UUID entityId) { this.entityId = entityId; }
  public Instant getChangedAt() { return changedAt; }
  public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
}
