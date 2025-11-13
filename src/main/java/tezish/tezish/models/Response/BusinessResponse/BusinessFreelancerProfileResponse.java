package tezish.tezish.models.Response.BusinessResponse;

import lombok.Data;
import tezish.tezish.models.Response.FreelancerResponse.WorkExperienceDTO;

import java.util.List;

@Data
public class BusinessFreelancerProfileResponse {
    private String fullName;
    private int age;
    private String profilePicture;
    private boolean verified;
    private String moreInfo;
    private String education;
    private List<String> languages;
    private List<WorkExperienceDTO> workExperience;
    private String coordinates;
    private Double radius;
    private boolean isDeleted;
    private int completedWorkCount;
    private int currentStreak;
    private int longestStreak;
}
