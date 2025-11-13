package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;
import tezish.tezish.models.Job_Shift.ShiftApplicationStatus;

import java.time.LocalDateTime;

@Data
public class FreelancerApplicationDTO {
    private Long freelancerId;
    private Long shiftApplicationId;
    private String name;
    private ShiftApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private boolean isDeleted;

    public FreelancerApplicationDTO(Long id, String name, ShiftApplicationStatus status,
                                    LocalDateTime appliedAt, LocalDateTime acceptedAt,
                                    LocalDateTime checkInTime, LocalDateTime checkOutTime, Long shiftApplicationId) {
        this.freelancerId = id;
        this.name = name;
        this.status = status;
        this.appliedAt = appliedAt;
        this.acceptedAt = acceptedAt;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.shiftApplicationId = shiftApplicationId;
    }

    public FreelancerApplicationDTO() {

    }
}
