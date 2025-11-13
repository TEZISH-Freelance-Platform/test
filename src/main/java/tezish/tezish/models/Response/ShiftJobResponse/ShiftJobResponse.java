package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShiftJobResponse {
    private Long shiftId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String startTime;
    private String endTime;

    public ShiftJobResponse(Long shiftId, LocalDateTime startDate, LocalDateTime endDate, String startTime, String endTime) {
        this.shiftId = shiftId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}

