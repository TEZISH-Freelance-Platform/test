package tezish.tezish.models.Response.FreelancerResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EducationExperienceLanguageResponse {
    private String education;
    private List<EducationExperienceLanguageRequest.Experience> jobExperience;
    private List<String> languages;
    private String healthCardImage;
    private String healthCardExpirationDate;
}

