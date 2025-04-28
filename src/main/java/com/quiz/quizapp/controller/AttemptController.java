// AttemptController.java
package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.Quiz;
import com.quiz.quizapp.model.QuizAttempt;
import com.quiz.quizapp.repository.QuizAttemptRepository;
import com.quiz.quizapp.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/attempts")
@CrossOrigin(origins = "http://localhost:5173") // Adapte ton frontend ici
@RequiredArgsConstructor
public class AttemptController {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository attemptRepository;

    @PostMapping("/submit")
    public QuizAttempt submitAttempt(@RequestBody QuizAttempt attempt) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(attempt.getQuizId());

        if (optionalQuiz.isEmpty()) {
            throw new RuntimeException("Quiz not found");
        }

        Quiz quiz = optionalQuiz.get();
        int correctCount = 0;

        for (var question : quiz.getQuestions()) {
            String correctAnswer = question.getCorrectAnswer();
            String userAnswer = attempt.getAnswers().get(question.getId());

            if (correctAnswer != null && userAnswer != null) {
                if (question.getType().equals("multiple-choice") || question.getType().equals("true-false")) {
                    if (correctAnswer.equals(userAnswer)) {
                        correctCount++;
                    }
                } else if (question.getType().equals("short-answer")) {
                    if (correctAnswer.trim().equalsIgnoreCase(userAnswer.trim())) {
                        correctCount++;
                    }
                }
            }
        }

        int score = (correctCount * 100) / quiz.getQuestions().size();
        attempt.setScore(score);
        attempt.setCompletedAt(new Date());

        return attemptRepository.save(attempt);
    }


    @GetMapping("/user/{userId}")
    public java.util.List<QuizAttempt> getUserAttempts(@PathVariable String userId) {
        return attemptRepository.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public Optional<QuizAttempt> getAttemptById(@PathVariable String id) {
        return attemptRepository.findById(id);
    }
}
