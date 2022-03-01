package com.comittedpeople.englishlearningweb.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface QuizService {

    /**
     * Generate a quiz from a vocabulary lesson.
     *
     * @param lessonId       the vocabulary lesson ID
     * @param questionCount  number of questions to generate
     * @return a QuizSession containing the questions
     */
    QuizSession generateVocabQuiz(Long lessonId, int questionCount);

    /**
     * Generate a quiz from a grammar category.
     *
     * @param categoryId    the grammar category ID
     * @param questionCount number of questions to generate
     * @return a QuizSession containing the questions
     */
    QuizSession generateGrammarQuiz(Long categoryId, int questionCount);

    /**
     * Submit an answer for a quiz question.
     *
     * @param sessionId  the quiz session ID
     * @param questionId the question index
     * @param answer     the user's answer
     * @return true if the answer is correct
     */
    boolean submitAnswer(String sessionId, int questionId, String answer);

    /**
     * Get the results of a completed quiz session.
     *
     * @param sessionId the quiz session ID
     * @return a map containing score, total, percentage, and question details
     */
    Map<String, Object> getQuizResults(String sessionId);

    /**
     * Get a specific quiz session by its ID.
     *
     * @param sessionId the session ID
     * @return the quiz session, or null if not found
     */
    QuizSession getQuizSession(String sessionId);

    /**
     * Represents a quiz session with its questions and state.
     */
    class QuizSession {
        private String sessionId;
        private Long userId;
        private String quizType;
        private List<QuizQuestion> questions;
        private int score;
        private boolean completed;

        public QuizSession() {}

        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getQuizType() { return quizType; }
        public void setQuizType(String quizType) { this.quizType = quizType; }
        public List<QuizQuestion> getQuestions() { return questions; }
        public void setQuestions(List<QuizQuestion> questions) { this.questions = questions; }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }

    /**
     * Represents a single quiz question.
     */
    class QuizQuestion {
        private int questionIndex;
        private String questionText;
        private List<String> options;
        private String correctAnswer;
        private String userAnswer;
        private boolean answered;

        public QuizQuestion() {}

        public int getQuestionIndex() { return questionIndex; }
        public void setQuestionIndex(int questionIndex) { this.questionIndex = questionIndex; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
        public String getUserAnswer() { return userAnswer; }
        public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
        public boolean isAnswered() { return answered; }
        public void setAnswered(boolean answered) { this.answered = answered; }
    }
}
