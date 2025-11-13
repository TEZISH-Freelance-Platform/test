package tezish.tezish.models.Job_Shift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import tezish.tezish.models.Company.BusinessUser;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class ShiftApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonManagedReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    private boolean paid;
    private String coordinates;
    private String coordinatesCheckOut;

    @Enumerated(EnumType.STRING)
    private ShiftApplicationStatus status;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    @Column(name = "checkin_time") 
    private LocalDateTime checkInTime;

    @Column(name = "checkout_time")
    private LocalDateTime checkOutTime;

    @Column(name = "overtime")
    private String overtime;

    @Column(name = "shift_price")
    private BigDecimal shiftPrice;

    @Column(name = "job_image")
    private String jobImage;

    @Column(
            name="one_day_before",
            nullable=false,
            columnDefinition="boolean default false"
    )
    private boolean notifiedOneDayBefore;

    @Column(
            name="one_hour_before",
            nullable=false,
            columnDefinition="boolean default false"
    )
    private boolean notifiedOneHourBefore;

    @Column(
            name="fifteen_minute_before",
            nullable=false,
            columnDefinition="boolean default false"
    )
    private boolean notifiedFifteenMinBefore;

    @Column(
            name="five_minute_before",
            nullable=false,
            columnDefinition="boolean default false"
    )
    private boolean notifiedFiveMinBeforeEnd;

    @Column(
            name="waiting_acceptance_notified",
            nullable=false,
            columnDefinition="boolean default false"
    )
    private boolean notifiedWaitingForAcceptance;

    @JsonManagedReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "accepted_by_user_id")
    private BusinessUser acceptedBy;
}
