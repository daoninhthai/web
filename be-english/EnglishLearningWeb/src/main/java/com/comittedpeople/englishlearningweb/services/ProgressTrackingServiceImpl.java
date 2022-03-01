package com.comittedpeople.englishlearningweb.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ProgressTrackingServiceImpl implements ProgressTrackingService {

    private static final Logger logger = LoggerFactory.getLogger(ProgressTrackingServiceImpl.class);

    // User progress data structures
    private final Map<Long, List<LessonRecord>> lessonRecords = new ConcurrentHashMap<>();
    private final Map<Long, List<QuizRecord>> quizRecords = new ConcurrentHashMap<>();
    private final Map<Long, Set<LocalDate>> studyDays = new ConcurrentHashMap<>();
    private final Map<Long, Long> studyTimeMinutes = new ConcurrentHashMap<>();

    @Override
    public void recordLessonCompletion(Long userId, Long lessonId, int score) {
        int clampedScore = Math.max(0, Math.min(100, score));
        lessonRecords.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(new LessonRecord(lessonId, clampedScore, LocalDate.now()));

        recordDailyStudy(userId);
        logger.info("User {} completed lesson {} with score {}", userId, lessonId, clampedScore);
    }

    @Override
    public void recordQuizCompletion(Long userId, String quizType, int score, int totalQuestions) {
        quizRecords.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(new QuizRecord(quizType, score, totalQuestions, LocalDate.now()));

        recordDailyStudy(userId);
        logger.info("User {} completed {} quiz: {}/{}", userId, quizType, score, totalQuestions);
    }

    @Override
    public Map<String, Object> getProgressSummary(Long userId) {
        Map<String, Object> summary = new HashMap<>();

        List<LessonRecord> lessons = lessonRecords.getOrDefault(userId, Collections.emptyList());
        List<QuizRecord> quizzes = quizRecords.getOrDefault(userId, Collections.emptyList());

        summary.put("userId", userId);
        summary.put("totalLessonsCompleted", lessons.size());
        summary.put("totalQuizzesTaken", quizzes.size());

        // Calculate average quiz score
        if (!quizzes.isEmpty()) {
            double avgScore = quizzes.stream()
                    .mapToDouble(q -> (double) q.score / q.totalQuestions * 100)
                    .average()
                    .orElse(0.0);
            summary.put("averageQuizScore", Math.round(avgScore * 10.0) / 10.0);
        } else {
            summary.put("averageQuizScore", 0.0);
        }

        // Calculate average lesson score
        if (!lessons.isEmpty()) {
            double avgLessonScore = lessons.stream()
                    .mapToInt(l -> l.score)
                    .average()
                    .orElse(0.0);
            summary.put("averageLessonScore", Math.round(avgLessonScore * 10.0) / 10.0);
        } else {
            summary.put("averageLessonScore", 0.0);
        }

        summary.put("currentStreak", getStudyStreak(userId));
        summary.put("totalStudyTimeMinutes", getTotalStudyTime(userId));
        summary.put("uniqueLessonsCompleted", getCompletedLessons(userId).size());

        logger.debug("Progress summary for user {}: {} lessons, {} quizzes",
                userId, lessons.size(), quizzes.size());
        return summary;
    }

    @Override
    public List<Long> getCompletedLessons(Long userId) {
        return lessonRecords.getOrDefault(userId, Collections.emptyList()).stream()
                .map(r -> r.lessonId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public int getStudyStreak(Long userId) {
        Set<LocalDate> days = studyDays.get(userId);
        if (days == null || days.isEmpty()) {
            return 0;
        }

        List<LocalDate> sortedDays = new ArrayList<>(days);
        Collections.sort(sortedDays, Comparator.reverseOrder());

        LocalDate today = LocalDate.now();
        if (!sortedDays.get(0).equals(today) && !sortedDays.get(0).equals(today.minusDays(1))) {
            return 0;
        }

        int streak = 1;
        for (int i = 0; i < sortedDays.size() - 1; i++) {
            long daysBetween = ChronoUnit.DAYS.between(sortedDays.get(i + 1), sortedDays.get(i));
            if (daysBetween == 1) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    @Override
    public void recordDailyStudy(Long userId) {
        studyDays.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
                .add(LocalDate.now());
    }

    @Override
    public long getTotalStudyTime(Long userId) {
        return studyTimeMinutes.getOrDefault(userId, 0L);
    }

    @Override
    public void recordStudyTime(Long userId, int minutes) {
        if (minutes > 0) {
            studyTimeMinutes.merge(userId, (long) minutes, Long::sum);
            recordDailyStudy(userId);
            logger.debug("User {} recorded {} minutes of study. Total: {}",
                    userId, minutes, studyTimeMinutes.get(userId));
        }
    }

    private static class LessonRecord {
        Long lessonId;
        int score;
        LocalDate date;

        LessonRecord(Long lessonId, int score, LocalDate date) {
            this.lessonId = lessonId;
            this.score = score;
            this.date = date;
        }
    }

    private static class QuizRecord {
        String quizType;
        int score;
        int totalQuestions;
        LocalDate date;

        QuizRecord(String quizType, int score, int totalQuestions, LocalDate date) {
            this.quizType = quizType;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.date = date;
        }
    }
}
