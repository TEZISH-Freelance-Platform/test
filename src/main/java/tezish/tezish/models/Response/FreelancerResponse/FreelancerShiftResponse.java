package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FreelancerShiftResponse {
    private Long shiftId;
    private String jobName;
    private BigDecimal shiftPrice;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String jobImage;
}
