package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShiftCardResponse {
    private String companyName;
    private String jobTitle;
    private String shiftStartTime;
    private String shiftEndTime;
    private BigDecimal shiftPrice;
}
