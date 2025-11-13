package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;
import tezish.tezish.models.Job_Shift.ShiftApplicationStatus;

import java.time.LocalDateTime;

@Data
public class ShiftApplicationStatusUpdateResponse {
    private Long shiftApplicationId;
    private Long freelancerId;
    private Long shiftId;
    private ShiftApplicationStatus previousStatus;
    private ShiftApplicationStatus newStatus;
    private LocalDateTime updatedAt;
    private String message;
    private boolean success;

    public ShiftApplicationStatusUpdateResponse(Long shiftApplicationId, Long freelancerId, Long shiftId, 
                                              ShiftApplicationStatus previousStatus, ShiftApplicationStatus newStatus, 
                                              LocalDateTime updatedAt, String message, boolean success) {
        this.shiftApplicationId = shiftApplicationId;
        this.freelancerId = freelancerId;
        this.shiftId = shiftId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.updatedAt = updatedAt;
        this.message = message;
        this.success = success;
    }

    public ShiftApplicationStatusUpdateResponse() {
    }
} 