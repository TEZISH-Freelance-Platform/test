package tezish.tezish.models.Job_Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftTemplateResponse {
    private Long id;
    private String shiftLocation;
    private BigDecimal price;
    private int freelancerCount;
    private String position;
    private String jobImage;
    private String emoji;
    private Long manager;
    private String additionalInfo;
    private boolean providesTraining;
    private boolean providesMeal;
    private boolean requiresID;
    private boolean providesTravelAllowance;
    private boolean providesDress;
    private boolean requiredDress;
    private Long companyId;
    private String companyName;
} 