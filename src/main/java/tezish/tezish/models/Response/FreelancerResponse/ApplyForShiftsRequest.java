package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

import java.util.List;

@Data
public class ApplyForShiftsRequest {
    private Long freelancerId;
    private List<Long> shiftIds;

}
