package fr.mycellius.persistence.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findByPageIdOrderByCreatedAtDesc(String pageId);
}