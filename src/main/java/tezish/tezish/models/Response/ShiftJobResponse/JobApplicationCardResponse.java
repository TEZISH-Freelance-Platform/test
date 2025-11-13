package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;
import tezish.tezish.models.Job_Shift.ShiftApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobApplicationCardResponse {
    private long id;
    private String jobTitle;
    private BigDecimal price;
    private String shiftStartTime;
    private String shiftEndTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String companyName;
    private String businessUserPhoneNumber;
    private Double latitude;
    private Double longitude;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private ShiftApplicationStatus status;

    private boolean overtime;
    private boolean dressProvided;
    private boolean mealProvided;
    private boolean trainingProvided;
    private boolean idRequired;

    private boolean accepted;
}
