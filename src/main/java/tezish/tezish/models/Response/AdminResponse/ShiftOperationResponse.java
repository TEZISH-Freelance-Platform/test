package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShiftOperationResponse {
    private Long shiftId;
    private Long jobId;
    private String position;
    private String shiftLocation;
    private LocalDateTime shiftStartDate;
    private LocalDateTime shiftEndDate;
    private String shiftTime;
    private BigDecimal price;
    private boolean finished;
    private boolean filled;
    private boolean archived;
    private String companyName;
    
    // Nested counts object
    private ShiftCountsDTO counts;

}

