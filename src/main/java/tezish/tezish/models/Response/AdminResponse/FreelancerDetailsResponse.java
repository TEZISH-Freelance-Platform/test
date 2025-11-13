package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FreelancerDetailsResponse {

    private Long freelancerId;
    private String fullName;
    private List<WorkExperience> workExperience;
    private String education;
    private String moreInfo;
    private String profilePicture;
    private String verified;
    private List<String> phoneNumbers;
    private List<String> languages;
    private LocalDate dateOfBirth;

    private String coordinates;
    private Double radius;
    private boolean isDeleted;
    
    // Online status information
    private boolean isOnline;
    private String currentPage;
    private LocalDateTime lastOnlineAt;
    private LocalDateTime onlineSince;
    private Long totalOnlineTimeToday;
    
    // Streak information
    private int completedWorkCount;
    private int currentStreak;
    private int longestStreak;

    @Data
    public static class WorkExperience {
        private String jobTitle;
        private String companyName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String duration;

        public WorkExperience(String jobTitle, String companyName, LocalDate startDate, LocalDate endDate, String duration) {
            this.jobTitle = jobTitle;
            this.companyName = companyName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.duration = duration;
        }
    }

}

