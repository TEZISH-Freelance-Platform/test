package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JobRequest {
    private String position;
    private BigDecimal price;
    private int freelancerCount;
    private Long locationId;
    private Long statusId;
    private String additionalInfo;
    private boolean providesTraining;
    private boolean providesMeal;
    private boolean requiresID;
    private boolean providesTravelAllowance;
    private boolean providesDress;
    private boolean requiredDress;
    private boolean requiredHealthCard;
    private Long managerId;
    private List<Long> managerIds;
    private List<ShiftRequest> shifts;
}
