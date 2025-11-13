package tezish.tezish.models.Response.Invoice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FreelancerShiftInfoDTO {
    private Long freelancerId;
    private String freelancerName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private boolean isDeleted;
}
