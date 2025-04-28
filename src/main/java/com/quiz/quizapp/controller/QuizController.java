package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.Quiz;
import com.quiz.quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:5173")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    // ✅ Create a new Quiz (avec validation simple)
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        if (quiz.getTitle() == null || quiz.getTitle().isEmpty()) {
            throw new RuntimeException("Title is required.");
        }
        if (quiz.getCreatedBy() == null || quiz.getCreatedBy().isEmpty()) {
            throw new RuntimeException("CreatedBy (User ID) is required.");
        }
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            throw new RuntimeException("At least one question is required.");
        }
        return quizRepository.save(quiz);
    }

    // ✅ Get all quizzes
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // ✅ Get a specific quiz by ID
    @GetMapping("/{id}")
    public Optional<Quiz> getQuizById(@PathVariable String id) {
        return quizRepository.findById(id);
    }
}
