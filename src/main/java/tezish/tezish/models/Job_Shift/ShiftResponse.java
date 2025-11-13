package tezish.tezish.models.Job_Shift;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShiftResponse {
    private Long id;
    private String shiftLocation;
    private BigDecimal price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int freelancerCount;
    private String startTime;
    private String endTime;
    private String position;
    private String jobImage;
    private String emoji;
}
