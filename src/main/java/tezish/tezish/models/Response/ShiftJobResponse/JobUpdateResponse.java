package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobUpdateResponse {
    private Long id;
    private String position;
    private BigDecimal price;
    private int freelancerCount;
    private Long locationId;
    private Long statusId;
    private String message;

    public JobUpdateResponse(Long id, String position, BigDecimal price, int freelancerCount, Long locationId, Long statusId) {
        this.id = id;
        this.position = position;
        this.price = price;
        this.freelancerCount = freelancerCount;
        this.locationId = locationId;
        this.statusId = statusId;
        this.message = "Job updated successfully";
    }
}

