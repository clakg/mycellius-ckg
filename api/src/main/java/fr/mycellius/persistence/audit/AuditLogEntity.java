package fr.mycellius.persistence.audit;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_log")
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String action;

    @Column(name = "page_id", length = 64)
    private String pageId;

    @Column(nullable = false, length = 64)
    private String actor;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected AuditLogEntity() {}

    public AuditLogEntity(String action, String pageId, String actor) {
        this.action = action;
        this.pageId = pageId;
        this.actor = actor;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getAction() { return action; }
    public String getPageId() { return pageId; }
    public String getActor() { return actor; }
    public Instant getCreatedAt() { return createdAt; }
}