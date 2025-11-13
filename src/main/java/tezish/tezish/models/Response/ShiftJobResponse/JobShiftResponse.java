package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobShiftResponse {
    private Long jobId;
    private String position;
    private Long locationId;
    private String shiftLocation;
    private LocalDateTime shiftStartDate;
    private LocalDateTime shiftEndDate;
    private String shiftTime;
    private int freelancerCount;
    private BigDecimal price;
    private boolean finished;
    private boolean filled;
    private boolean providesMeal;
    private boolean providesTravelAllowance;
    private boolean requiresID;
    private boolean providesTraining;
    private boolean requiredHealthCard;
    private String additionalInfo;
    private Long companyId;
    private Long manager;
    private List<ShiftJobResponse> shifts;
    private Long createdByBusinessUser;

}
