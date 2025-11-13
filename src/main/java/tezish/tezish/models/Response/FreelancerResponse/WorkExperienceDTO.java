package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkExperienceDTO {
    private Long id;
    private String jobTitle;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String duration;

    public WorkExperienceDTO(Long id, String jobTitle, String companyName, LocalDate startDate, LocalDate endDate, String duration) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

}

