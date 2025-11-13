package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

@Data
public class LocationUpdateRequest {
    private String coordinates;
    private Double radius;
} 