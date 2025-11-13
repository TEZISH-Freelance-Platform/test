package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShiftDetailsForAdmin {
    private Long shiftId;
    private String jobTitle;
    private BigDecimal shiftPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String startTime;
    private String endTime;
    private String location;
    private int freelancerCount;
    private long totalApplicants;
    private long acceptedApplicants;
    private long pendingApplicants;
    private boolean isProvidesDress;
    private boolean isProvidesTravelAllowance;
    private boolean isRequiresID;
    private boolean isProvidesMeal;
    private boolean isProvidesTraining;
    private boolean isRequiredHealthCard;
    private String coordinates;
    private String additionalInfo;
    private String jobImage;
    private String managerName;
    private String managerPhone;
    private String jobEmoji;
    private boolean archived;
}

