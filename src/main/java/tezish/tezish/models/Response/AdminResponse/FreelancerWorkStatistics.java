package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;
import java.util.List;

@Data
public class FreelancerWorkStatistics {
    private Long freelancerId;
    private String freelancerName;
    private long totalCompletedShifts;
    
    // Per company statistics
    private List<CompanyWorkStatistics> companyStatistics;
    
    @Data
    public static class CompanyWorkStatistics {
        private Long companyId;
        private String companyName;
        private long completedShifts;
    }
}
