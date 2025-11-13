package tezish.tezish.models.Scheduler;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Dedicated lock table for distributed job execution
 * Uses unique constraint to prevent race conditions
 */
@Data
@Entity
@Table(name = "scheduled_job_lock",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_job_name", columnNames = {"job_name"})
       })
public class ScheduledJobLock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_name", nullable = false, unique = true, length = 100)
    private String jobName;
    
    @Column(name = "locked_at", nullable = false)
    private LocalDateTime lockedAt;
    
    @Column(name = "locked_by_instance", length = 255)
    private String lockedByInstance;
    
    @Column(name = "execution_id")
    private Long executionId;
    
    @PrePersist
    protected void onCreate() {
        if (lockedAt == null) {
            lockedAt = LocalDateTime.now();
        }
    }
}

