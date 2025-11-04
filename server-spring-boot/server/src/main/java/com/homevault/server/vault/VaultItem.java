package com.homevault.server.vault;

import com.homevault.server.crypto.EncryptedPayload;
import jakarta.persistence.*;
import java.util.UUID;

@Entity// directly maps to DB table
@Table(name = "vault_items")
public class VaultItem {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String username;

  @Embedded
  private EncryptedPayload payload;

  @Transient
  private String plaintextSecret; //not persisted, only exits in memory during runtime. stores plain-text password but never saved

  public VaultItem() {} //default contructor

  public VaultItem(String title, String username) {
    this.title = title;
    this.username = username;
  }
  
  //getters and setters
  public UUID getId() { return id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public EncryptedPayload getPayload() { return payload; }
  public void setPayload(EncryptedPayload payload) { this.payload = payload; }

  public String getPlaintextSecret() { return plaintextSecret; }
  public void setPlaintextSecret(String plaintextSecret) { this.plaintextSecret = plaintextSecret; }
}
