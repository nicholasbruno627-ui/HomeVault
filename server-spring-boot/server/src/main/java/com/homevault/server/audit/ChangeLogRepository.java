package com.homevault.server.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, UUID> {
  List<ChangeLog> findByUserIdOrderByChangedAtDesc(UUID userId); //get logs for all users, newest on top
  List<ChangeLog> findByEntityTypeAndEntityIdOrderByChangedAtDesc(String entityType, UUID entityId); //logs for a single entity
  List<ChangeLog> findByChangedAtBetweenOrderByChangedAtDesc(Instant from, Instant to); //logs in between times
}
