package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateResponse {
    private Long id;
    private String position;
    private BigDecimal price;
    private int freelancerCount;
    private Long locationId;
    private Long statusId;
    private List<Long> shiftIds;
    private String message;

    public JobCreateResponse(Long id, String position, BigDecimal price, int freelancerCount, 
                            Long locationId, Long statusId, List<Long> shiftIds) {
        this.id = id;
        this.position = position;
        this.price = price;
        this.freelancerCount = freelancerCount;
        this.locationId = locationId;
        this.statusId = statusId;
        this.shiftIds = shiftIds;
        this.message = "Job created successfully";
    }
}

