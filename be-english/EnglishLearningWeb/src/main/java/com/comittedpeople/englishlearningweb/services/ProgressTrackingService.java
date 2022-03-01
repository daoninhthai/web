package com.comittedpeople.englishlearningweb.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProgressTrackingService {

    /**
     * Record that a user completed a vocabulary lesson.
     *
     * @param userId   the user ID
     * @param lessonId the lesson ID
     * @param score    the score achieved (0-100)
     */
    void recordLessonCompletion(Long userId, Long lessonId, int score);

    /**
     * Record that a user completed a quiz.
     *
     * @param userId    the user ID
     * @param quizType  the type of quiz (VOCABULARY, GRAMMAR)
     * @param score     the score achieved
     * @param totalQuestions the total number of questions
     */
    void recordQuizCompletion(Long userId, String quizType, int score, int totalQuestions);

    /**
     * Get the overall progress summary for a user.
     *
     * @param userId the user ID
     * @return a map containing progress statistics
     */
    Map<String, Object> getProgressSummary(Long userId);

    /**
     * Get the list of lessons completed by a user.
     *
     * @param userId the user ID
     * @return list of completed lesson IDs
     */
    List<Long> getCompletedLessons(Long userId);

    /**
     * Get the user's daily study streak (consecutive days).
     *
     * @param userId the user ID
     * @return the current streak count
     */
    int getStudyStreak(Long userId);

    /**
     * Record that the user studied today (for streak tracking).
     *
     * @param userId the user ID
     */
    void recordDailyStudy(Long userId);

    /**
     * Get the total study time (in minutes) for a user.
     *
     * @param userId the user ID
     * @return total study time in minutes
     */
    long getTotalStudyTime(Long userId);

    /**
     * Record study time for a session.
     *
     * @param userId  the user ID
     * @param minutes the number of minutes studied
     */
    void recordStudyTime(Long userId, int minutes);
}
