package com.homevault.server.backup;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BackupRepository extends JpaRepository<Backup, UUID> {
  List<Backup> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId); //find by UUID from most recent first
}
