package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShiftDetails {
    private boolean requiredDress;
    private String additionalInfo;
    private boolean isProvidesDress;
    private boolean isProvidesTravelAllowance;
    private boolean isRequiresID;
    private boolean isProvidesMeal;
    private boolean isProvidesTraining;
    private boolean isRequiredHealthCard;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String startTime;
    private String endTime;
    private BigDecimal price;
    private String applicationStatus;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}