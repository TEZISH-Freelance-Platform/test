package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

@Data
public class FreelancerExperienceRequest {
    private Long id;
    private String jobTitle;
    private String companyName;
    private String duration;
}
