package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;

@Data
public class FreelancerProfileResponse {

    private String fullName;
    private int age;
    private String profilePicture;
    private boolean verified;
    private String coordinates;
    private Double radius;
    private int completedWorkCount;
    private int currentStreak;
    private int longestStreak;

    // Constructors
    public FreelancerProfileResponse(String fullName, int age, String profilePicture, boolean verified, String coordinates, Double radius, int completedWorkCount, int currentStreak, int longestStreak) {
        this.fullName = fullName;
        this.age = age;
        this.profilePicture = profilePicture;
        this.verified = verified;
        this.coordinates = coordinates;
        this.radius = radius;
        this.completedWorkCount = completedWorkCount;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
    }
}
