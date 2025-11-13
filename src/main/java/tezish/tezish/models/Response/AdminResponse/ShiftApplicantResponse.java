package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

import java.util.List;

@Data
public class ShiftApplicantResponse {
    private Long freelancerId;
    private String fullName;
    private String profilePicture;
    private String status;
    private String contract;
    private String checkIn;
    private String checkOut;
    private String payment;
    private int shiftPersonalCount;
    private Long shiftApplicationId;
    private String coordinates;
    private String coordinatesCheckOut;
    private List<String> freelancerPhones;
    private String adminNote;

    private String freelancerCoordinates;
    private Double freelancerRadius;
    private int age;
    private int completedWorkCount; // Number of finished jobs by the freelancer
    private String acceptedAt; // When the freelancer accepted the work
    private boolean isDeleted;
    
    // Work statistics fields
    private long totalCompletedShifts;
    private List<CompanyWorkStatistics> companyStatistics;
    
    // Health card info
    private boolean hasHealthCard; // at least one health card exists
    private String healthCardImage; // latest health card image URL
    private String healthCardExpirationDate; // ISO date string of latest health card expiration
    private boolean isHealthCardVerified; // latest health card verified status
    
    @Data
    public static class CompanyWorkStatistics {
        private Long companyId;
        private String companyName;
        private long completedShifts;
    }
}

