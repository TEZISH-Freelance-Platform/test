package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;

@Data
public class ShiftApplicationCheckTimeRequest {
    private String checkIn;
    private String checkOut;
} 