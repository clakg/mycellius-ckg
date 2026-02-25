package fr.mycellius.service;

import fr.mycellius.persistence.audit.AuditLogEntity;
import fr.mycellius.persistence.audit.AuditLogJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogJpaRepository repo;

    public AuditService(AuditLogJpaRepository repo) {
        this.repo = repo;
    }

    public void log(String action, String pageId, String actor) {
        repo.save(new AuditLogEntity(action, pageId, actor));
    }

    public List<AuditLogEntity> listByPageId(String pageId) {
        return repo.findByPageIdOrderByCreatedAtDesc(pageId);
    }
}