package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EducationExperienceLanguageRequest {
    private String education;
    private List<Experience> jobExperience;
    private List<String> languages;

    @Data
    public static class Experience {
        private Long id;
        private String jobTitle;
        private String companyName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String duration;

        public Experience(Long id, String jobTitle, String companyName, LocalDate startDate, LocalDate endDate, String duration) {
            this.id=id;
            this.companyName=companyName;
            this.jobTitle=jobTitle;
            this.startDate=startDate;
            this.endDate=endDate;
            this.duration=duration;
        }
    }
}

