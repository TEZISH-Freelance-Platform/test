package tezish.tezish.models.Job_Shift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Column(nullable = false)
    private boolean paymentStatus;
    @Column(nullable = false)
    private java.math.BigDecimal price;

    @JsonManagedReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private JobStatus status;

    @JsonManagedReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShiftApplication> applications = new ArrayList<>();

    @Column(nullable = false)
    private boolean archived = false;

    @Column(name = "is_priority", nullable = false)
    private boolean isPriority = false;

    @Column(name = "priority_expires_at")
    private LocalDateTime priorityExpiresAt;

	@Column(name = "visible_window_notified", nullable = false, columnDefinition = "boolean default false")
	private boolean visibleWindowNotified = false;

	@Column(name = "visible_window_notify_at")
	private LocalDateTime visibleWindowNotifyAt;

	@Column(name = "visible_window_notification_scheduled", nullable = false, columnDefinition = "boolean default false")
	private boolean visibleWindowNotificationScheduled = false;

	@Column(name = "message")
	private String message;

}
