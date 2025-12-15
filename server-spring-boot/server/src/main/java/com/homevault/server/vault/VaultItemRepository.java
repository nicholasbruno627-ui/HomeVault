package com.homevault.server.vault;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VaultItemRepository extends JpaRepository<VaultItem, UUID> {

    //for listing a user's items
    List<VaultItem> findByUserIdOrderByCreatedAtAsc(UUID userId);

    //for loading a single item, making sure it belongs to that user
    Optional<VaultItem> findByIdAndUserId(UUID id, UUID userId);
}
