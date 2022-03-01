package com.comittedpeople.englishlearningweb.controllers.v1;

import com.comittedpeople.englishlearningweb.services.QuizService;
import com.comittedpeople.englishlearningweb.services.QuizService.QuizSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class QuizController {

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/api/v1/quiz/vocab/{lessonId}")
    public ResponseEntity<QuizSession> generateVocabQuiz(
            @PathVariable Long lessonId,
            @RequestParam(defaultValue = "10") int count) {

        logger.info("Request to generate vocab quiz for lesson {} with {} questions", lessonId, count);
        QuizSession session = quizService.generateVocabQuiz(lessonId, count);

        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/quiz/grammar/{categoryId}")
    public ResponseEntity<QuizSession> generateGrammarQuiz(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "10") int count) {

        logger.info("Request to generate grammar quiz for category {} with {} questions", categoryId, count);
        QuizSession session = quizService.generateGrammarQuiz(categoryId, count);

        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/quiz/{sessionId}/answer/{questionId}")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @PathVariable String sessionId,
            @PathVariable int questionId,
            @RequestBody Map<String, String> body) {

        String answer = body.get("answer");
        if (answer == null || answer.trim().isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Answer is required"), HttpStatus.BAD_REQUEST);
        }

        boolean correct = quizService.submitAnswer(sessionId, questionId, answer);
        return ResponseEntity.ok(Map.of(
                "correct", correct,
                "questionId", questionId
        ));
    }

    @GetMapping("/api/v1/quiz/{sessionId}/results")
    public ResponseEntity<Map<String, Object>> getQuizResults(@PathVariable String sessionId) {
        Map<String, Object> results = quizService.getQuizResults(sessionId);
        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/api/v1/quiz/{sessionId}")
    public ResponseEntity<QuizSession> getQuizSession(@PathVariable String sessionId) {
        QuizSession session = quizService.getQuizSession(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(session);
    }
}
