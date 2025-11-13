package tezish.tezish.models.Job_Shift;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OtherShift {
    private Long id;
    private BigDecimal price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String startTime;
    private String endTime;
    private String applicationStatus;
    private boolean priority;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public OtherShift(Long id, BigDecimal price, LocalDateTime startDate, LocalDateTime endDate, String startTime, String endTime, String applicationStatus, boolean priority, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        this.id = id;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.applicationStatus = applicationStatus;
        this.priority = priority;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }
}