package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShiftRequest {
    private Long shiftId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
