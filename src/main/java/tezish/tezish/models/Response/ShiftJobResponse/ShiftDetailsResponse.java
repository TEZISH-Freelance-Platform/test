package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDetailsResponse {
    private Long id;
    private Long jobId;
    private String position;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal price;
    private boolean archived;
}

