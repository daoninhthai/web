package com.comittedpeople.englishlearningweb.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface LeaderboardService {

    /**
     * Add or update a user's score on the leaderboard.
     *
     * @param userId      the user ID
     * @param displayName the user's display name
     * @param points      the points to add
     */
    void addPoints(Long userId, String displayName, int points);

    /**
     * Get the top N users on the global leaderboard.
     *
     * @param limit the maximum number of entries to return
     * @return ordered list of leaderboard entries (highest score first)
     */
    List<LeaderboardEntry> getTopUsers(int limit);

    /**
     * Get a specific user's rank and score.
     *
     * @param userId the user ID
     * @return the leaderboard entry for the user, or null if not ranked
     */
    LeaderboardEntry getUserRank(Long userId);

    /**
     * Get the weekly leaderboard (resets every week).
     *
     * @param limit maximum entries to return
     * @return list of leaderboard entries for the current week
     */
    List<LeaderboardEntry> getWeeklyLeaderboard(int limit);

    /**
     * Reset the weekly leaderboard scores.
     */
    void resetWeeklyLeaderboard();

    /**
     * Get leaderboard statistics.
     *
     * @return a map containing total participants, average score, etc.
     */
    Map<String, Object> getLeaderboardStats();

    /**
     * Represents a single entry in the leaderboard.
     */
    class LeaderboardEntry {
        private Long userId;
        private String displayName;
        private int totalPoints;
        private int rank;

        public LeaderboardEntry() {}

        public LeaderboardEntry(Long userId, String displayName, int totalPoints, int rank) {
            this.userId = userId;
            this.displayName = displayName;
            this.totalPoints = totalPoints;
            this.rank = rank;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public int getTotalPoints() { return totalPoints; }
        public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }
    }
}
