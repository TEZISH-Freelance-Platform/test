package tezish.tezish.models.Response;

import lombok.Data;

import java.util.List;

@Data
public class LeaderboardResponse {
    private List<LeaderboardEntry> currentStreakLeaderboard;
    private List<LeaderboardEntry> allTimeLeaderboard;
    private UserLeaderboardPosition userPosition;

    @Data
    public static class LeaderboardEntry {
        private Long freelancerId;
        private String freelancerName;
        private String profilePictureUrl;
        private int currentStreak;
        private int longestStreak;
        private int completedWorkCount;
        private Integer rank;

        public LeaderboardEntry(Long freelancerId, String freelancerName, String profilePictureUrl, 
                               int currentStreak, int longestStreak, int completedWorkCount, Integer rank) {
            this.freelancerId = freelancerId;
            this.freelancerName = freelancerName;
            this.profilePictureUrl = profilePictureUrl;
            this.currentStreak = currentStreak;
            this.longestStreak = longestStreak;
            this.completedWorkCount = completedWorkCount;
            this.rank = rank;
        }
    }

    @Data
    public static class UserLeaderboardPosition {
        private Integer currentStreakRank;
        private Integer allTimeRank;
        private int currentStreak;
        private int longestStreak;
        private int completedWorkCount;

        public UserLeaderboardPosition(Integer currentStreakRank, Integer allTimeRank, 
                                      int currentStreak, int longestStreak, int completedWorkCount) {
            this.currentStreakRank = currentStreakRank;
            this.allTimeRank = allTimeRank;
            this.currentStreak = currentStreak;
            this.longestStreak = longestStreak;
            this.completedWorkCount = completedWorkCount;
        }
    }
}

