package com.comittedpeople.englishlearningweb.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardServiceImpl.class);

    // Global leaderboard: userId -> entry data
    private final Map<Long, UserScore> globalScores = new ConcurrentHashMap<>();
    // Weekly leaderboard
    private final Map<Long, UserScore> weeklyScores = new ConcurrentHashMap<>();

    @Override
    public void addPoints(Long userId, String displayName, int points) {
        if (userId == null || points <= 0) {
            logger.warn("Invalid addPoints request: userId={}, points={}", userId, points);
            return;
        }

        // Update global scores
        globalScores.compute(userId, (id, existing) -> {
            if (existing == null) {
                return new UserScore(userId, displayName, points);
            }
            existing.totalPoints += points;
            if (displayName != null) {
                existing.displayName = displayName;
            }
            return existing;
        });

        // Update weekly scores
        weeklyScores.compute(userId, (id, existing) -> {
            if (existing == null) {
                return new UserScore(userId, displayName, points);
            }
            existing.totalPoints += points;
            if (displayName != null) {
                existing.displayName = displayName;
            }
            return existing;
        });

        logger.info("User {} ({}) earned {} points. Global total: {}",
                userId, displayName, points, globalScores.get(userId).totalPoints);
    }

    @Override
    public List<LeaderboardEntry> getTopUsers(int limit) {
        int maxResults = Math.max(1, Math.min(limit, 100));
        return buildRankedList(globalScores, maxResults);
    }

    @Override
    public LeaderboardEntry getUserRank(Long userId) {
        UserScore userScore = globalScores.get(userId);
        if (userScore == null) {
            logger.debug("User {} not found on leaderboard", userId);
            return null;
        }

        // Calculate rank by counting users with higher scores
        long higherCount = globalScores.values().stream()
                .filter(s -> s.totalPoints > userScore.totalPoints)
                .count();

        int rank = (int) higherCount + 1;

        return new LeaderboardEntry(userId, userScore.displayName, userScore.totalPoints, rank);
    }

    @Override
    public List<LeaderboardEntry> getWeeklyLeaderboard(int limit) {
        int maxResults = Math.max(1, Math.min(limit, 100));
        return buildRankedList(weeklyScores, maxResults);
    }

    @Override
    public void resetWeeklyLeaderboard() {
        int previousSize = weeklyScores.size();
        weeklyScores.clear();
        logger.info("Weekly leaderboard reset. Cleared {} entries.", previousSize);
    }

    @Override
    public Map<String, Object> getLeaderboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalParticipants", globalScores.size());

        if (!globalScores.isEmpty()) {
            int totalPoints = globalScores.values().stream()
                    .mapToInt(s -> s.totalPoints)
                    .sum();

            int maxPoints = globalScores.values().stream()
                    .mapToInt(s -> s.totalPoints)
                    .max()
                    .orElse(0);

            double avgPoints = globalScores.values().stream()
                    .mapToInt(s -> s.totalPoints)
                    .average()
                    .orElse(0.0);

            stats.put("totalPointsAwarded", totalPoints);
            stats.put("highestScore", maxPoints);
            stats.put("averageScore", Math.round(avgPoints * 10.0) / 10.0);
        } else {
            stats.put("totalPointsAwarded", 0);
            stats.put("highestScore", 0);
            stats.put("averageScore", 0.0);
        }

        stats.put("weeklyParticipants", weeklyScores.size());
        return stats;
    }

    private List<LeaderboardEntry> buildRankedList(Map<Long, UserScore> scores, int limit) {
        List<UserScore> sorted = scores.values().stream()
                .sorted(Comparator.comparingInt((UserScore s) -> s.totalPoints).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        List<LeaderboardEntry> entries = new ArrayList<>();
        AtomicInteger rank = new AtomicInteger(1);

        for (UserScore score : sorted) {
            entries.add(new LeaderboardEntry(
                    score.userId,
                    score.displayName,
                    score.totalPoints,
                    rank.getAndIncrement()
            ));
        }

        return entries;
    }

    private static class UserScore {
        Long userId;
        String displayName;
        int totalPoints;

        UserScore(Long userId, String displayName, int totalPoints) {
            this.userId = userId;
            this.displayName = displayName;
            this.totalPoints = totalPoints;
        }
    }
}
