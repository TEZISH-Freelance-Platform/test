package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FreelancerUpdateRequest {
    private String name;
    private String surname;
    private String email;
    private List<String> phones;
    private List<String> skills;
    private List<FreelancerExperienceRequest> experiences;
    private LocalDate dateOfBirth;
    private String moreInfo;
    private String education;
    private String finCode;
    private String healthCard;
    private String exampleWorks;
    private boolean verified;
    private List<FreelancerLanguageRequest> languages;
}
