package tezish.tezish.models.Scheduler;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity to track scheduled job executions
 * Helps monitor job health, detect failures, and prevent duplicates
 */
@Data
@Entity
@Table(name = "scheduled_job_execution",
       indexes = {
           @Index(name = "idx_job_name_start_time", columnList = "job_name,start_time"),
           @Index(name = "idx_job_name_status", columnList = "job_name,status"),
           @Index(name = "idx_start_time", columnList = "start_time")
       })
public class ScheduledJobExecution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_name", nullable = false, length = 100)
    private String jobName;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status; // RUNNING, SUCCESS, FAILED, RECOVERED
    
    @Column(name = "duration_ms")
    private Long durationMs;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

