package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;
import tezish.tezish.models.Job_Shift.ShiftApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShiftApplicationResponse {
    private Long id;
    private Long shiftId;
    private String companyName;
    private String businessUserPhoneNumber;
    private String locationCoordinates;
    private String jobTitle;
    private String shiftStartTime;
    private String shiftEndTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String jobImage;
    private String emoji;
    private BigDecimal shiftPrice;
    private ShiftApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private boolean isPaid;
    private String message;
    private ManagerInfo manager;

}
