package com.homevault.server.vault;

import com.homevault.server.crypto.EncryptedPayload;
import com.homevault.server.user.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
    name = "vault_items",
    indexes = {
        @Index(name = "ix_vault_items_owner", columnList = "owner_id")
    }
)
public class VaultItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String username;

    @Embedded
    private EncryptedPayload payload;

   

    protected VaultItem() {
    }

    public VaultItem(User owner, String title, String username, EncryptedPayload payload) {
        this.owner = owner;
        this.title = title;
        this.username = username;
        this.payload = payload;
    }

    //getters and setters

    public UUID getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EncryptedPayload getPayload() {
        return payload;
    }

    public void setPayload(EncryptedPayload payload) {
        this.payload = payload;
    }
}
