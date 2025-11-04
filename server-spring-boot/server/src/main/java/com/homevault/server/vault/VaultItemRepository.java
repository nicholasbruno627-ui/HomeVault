package com.homevault.server.vault;

import com.homevault.server.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface VaultItemRepository extends JpaRepository<VaultItem, UUID> {
  List<VaultItem> findByOwner(User owner); //find by creator
  List<VaultItem> findByOwnerId(UUID ownerId); //find by creators UUID
  List<VaultItem> findByOwnerIdAndTitleContainingIgnoreCase(UUID ownerId, String titlePart); //find by both
}
