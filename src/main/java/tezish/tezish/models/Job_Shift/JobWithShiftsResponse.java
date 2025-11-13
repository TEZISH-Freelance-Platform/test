package tezish.tezish.models.Job_Shift;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class JobWithShiftsResponse {
    private Long id;
    private String position;
    private BigDecimal price;
    private int freelancerCount;
    private String startTime;
    private String endTime;
    private String picture;
    private List<ShiftResponse> shifts;

}
