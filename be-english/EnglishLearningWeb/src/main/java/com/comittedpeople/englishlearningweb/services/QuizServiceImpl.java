package com.comittedpeople.englishlearningweb.services;

import com.comittedpeople.englishlearningweb.domain.DocVocabContent;
import com.comittedpeople.englishlearningweb.repositories.DocVocabContentRepository;
import com.comittedpeople.englishlearningweb.domain.DocGrammarContent;
import com.comittedpeople.englishlearningweb.repositories.DocGrammarContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);
    private static final int MAX_OPTIONS = 4;

    @Autowired
    private DocVocabContentRepository vocabContentRepository;

    @Autowired
    private DocGrammarContentRepository grammarContentRepository;

    // In-memory session store
    private final Map<String, QuizSession> sessionStore = new ConcurrentHashMap<>();

    @Override
    public QuizSession generateVocabQuiz(Long lessonId, int questionCount) {
        logger.info("Generating vocab quiz for lesson {} with {} questions", lessonId, questionCount);

        List<DocVocabContent> vocabItems = vocabContentRepository.findAll().stream()
                .filter(v -> v.getLesson() != null && v.getLesson().getId().equals(lessonId))
                .collect(Collectors.toList());

        if (vocabItems.isEmpty()) {
            logger.warn("No vocabulary content found for lesson {}", lessonId);
            return null;
        }

        Collections.shuffle(vocabItems);
        int count = Math.min(questionCount, vocabItems.size());

        List<QuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DocVocabContent vocab = vocabItems.get(i);
            QuizQuestion question = new QuizQuestion();
            question.setQuestionIndex(i);
            question.setQuestionText("What is the meaning of: " + vocab.getContent() + "?");
            question.setCorrectAnswer(vocab.getDescription() != null ? vocab.getDescription() : vocab.getContent());

            // Generate distractor options from other vocabulary items
            List<String> options = generateOptions(vocab.getDescription(), vocabItems, i);
            question.setOptions(options);
            question.setAnswered(false);
            questions.add(question);
        }

        QuizSession session = new QuizSession();
        session.setSessionId(UUID.randomUUID().toString().substring(0, 12));
        session.setQuizType("VOCABULARY");
        session.setQuestions(questions);
        session.setScore(0);
        session.setCompleted(false);

        sessionStore.put(session.getSessionId(), session);
        logger.info("Quiz session {} created with {} questions", session.getSessionId(), questions.size());
        return session;
    }

    @Override
    public QuizSession generateGrammarQuiz(Long categoryId, int questionCount) {
        logger.info("Generating grammar quiz for category {} with {} questions", categoryId, questionCount);

        List<DocGrammarContent> grammarContents = grammarContentRepository.findAll().stream()
                .filter(g -> g.getCategory() != null && g.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());

        if (grammarContents.isEmpty()) {
            logger.warn("No grammar content found for category {}", categoryId);
            return null;
        }

        Collections.shuffle(grammarContents);
        int count = Math.min(questionCount, grammarContents.size());

        List<QuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DocGrammarContent grammar = grammarContents.get(i);
            QuizQuestion question = new QuizQuestion();
            question.setQuestionIndex(i);
            question.setQuestionText("Grammar question about: " + grammar.getTitle());
            question.setCorrectAnswer(grammar.getTitle());
            question.setOptions(Arrays.asList(grammar.getTitle(), "Option B", "Option C", "Option D"));
            question.setAnswered(false);
            questions.add(question);
        }

        QuizSession session = new QuizSession();
        session.setSessionId(UUID.randomUUID().toString().substring(0, 12));
        session.setQuizType("GRAMMAR");
        session.setQuestions(questions);
        session.setScore(0);
        session.setCompleted(false);

        sessionStore.put(session.getSessionId(), session);
        logger.info("Grammar quiz session {} created with {} questions", session.getSessionId(), count);
        return session;
    }

    @Override
    public boolean submitAnswer(String sessionId, int questionId, String answer) {
        QuizSession session = sessionStore.get(sessionId);
        if (session == null || session.isCompleted()) {
            logger.warn("Invalid session {} or already completed", sessionId);
            return false;
        }

        if (questionId < 0 || questionId >= session.getQuestions().size()) {
            logger.warn("Invalid question index {} for session {}", questionId, sessionId);
            return false;
        }

        QuizQuestion question = session.getQuestions().get(questionId);
        if (question.isAnswered()) {
            logger.debug("Question {} already answered in session {}", questionId, sessionId);
            return false;
        }

        question.setUserAnswer(answer);
        question.setAnswered(true);

        boolean correct = question.getCorrectAnswer().equalsIgnoreCase(answer.trim());
        if (correct) {
            session.setScore(session.getScore() + 1);
        }

        // Check if all questions are answered
        boolean allAnswered = session.getQuestions().stream().allMatch(QuizQuestion::isAnswered);
        if (allAnswered) {
            session.setCompleted(true);
            logger.info("Quiz session {} completed. Score: {}/{}", sessionId,
                    session.getScore(), session.getQuestions().size());
        }

        return correct;
    }

    @Override
    public Map<String, Object> getQuizResults(String sessionId) {
        QuizSession session = sessionStore.get(sessionId);
        if (session == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> results = new HashMap<>();
        results.put("sessionId", session.getSessionId());
        results.put("quizType", session.getQuizType());
        results.put("totalQuestions", session.getQuestions().size());
        results.put("correctAnswers", session.getScore());
        results.put("completed", session.isCompleted());

        if (session.getQuestions().size() > 0) {
            double percentage = (double) session.getScore() / session.getQuestions().size() * 100;
            results.put("percentage", Math.round(percentage * 100.0) / 100.0);
        }

        return results;
    }

    @Override
    public QuizSession getQuizSession(String sessionId) {
        return sessionStore.get(sessionId);
    }

    private List<String> generateOptions(String correctAnswer, List<DocVocabContent> pool, int excludeIndex) {
        List<String> options = new ArrayList<>();
        options.add(correctAnswer != null ? correctAnswer : "");

        Random random = new Random();
        Set<Integer> usedIndices = new HashSet<>();
        usedIndices.add(excludeIndex);

        while (options.size() < MAX_OPTIONS && usedIndices.size() < pool.size()) {
            int idx = random.nextInt(pool.size());
            if (!usedIndices.contains(idx)) {
                usedIndices.add(idx);
                String desc = pool.get(idx).getDescription();
                if (desc != null && !options.contains(desc)) {
                    options.add(desc);
                }
            }
        }

        // Pad with generic options if not enough vocabulary items
        while (options.size() < MAX_OPTIONS) {
            options.add("Option " + (options.size() + 1));
        }

        Collections.shuffle(options);
        return options;
    }
}
