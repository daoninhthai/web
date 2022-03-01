package com.comittedpeople.englishlearningweb.controllers.v1;

import com.comittedpeople.englishlearningweb.services.LeaderboardService;
import com.comittedpeople.englishlearningweb.services.LeaderboardService.LeaderboardEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class LeaderboardController {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/api/v1/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getGlobalLeaderboard(
            @RequestParam(defaultValue = "20") int limit) {
        List<LeaderboardEntry> entries = leaderboardService.getTopUsers(limit);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/api/v1/leaderboard/weekly")
    public ResponseEntity<List<LeaderboardEntry>> getWeeklyLeaderboard(
            @RequestParam(defaultValue = "20") int limit) {
        List<LeaderboardEntry> entries = leaderboardService.getWeeklyLeaderboard(limit);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/api/v1/leaderboard/user/{userId}")
    public ResponseEntity<LeaderboardEntry> getUserRank(@PathVariable Long userId) {
        LeaderboardEntry entry = leaderboardService.getUserRank(userId);
        if (entry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(entry);
    }

    @PostMapping("/api/v1/leaderboard/points")
    public ResponseEntity<Map<String, String>> addPoints(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        String displayName = body.getOrDefault("displayName", "Anonymous").toString();
        int points = Integer.parseInt(body.get("points").toString());

        if (points <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "Points must be positive"),
                    HttpStatus.BAD_REQUEST
            );
        }

        leaderboardService.addPoints(userId, displayName, points);
        logger.info("Points added via API: user={}, points={}", userId, points);
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @GetMapping("/api/v1/leaderboard/stats")
    public ResponseEntity<Map<String, Object>> getLeaderboardStats() {
        Map<String, Object> stats = leaderboardService.getLeaderboardStats();
        return ResponseEntity.ok(stats);
    }
}
