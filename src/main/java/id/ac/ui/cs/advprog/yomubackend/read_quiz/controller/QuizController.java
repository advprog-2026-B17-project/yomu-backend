package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.*;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final UserRepository userRepository;

    @Autowired
    public QuizController(QuizService quizService, UserRepository userRepository) {
        this.quizService = quizService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(@AuthenticationPrincipal UserDetails principal) {
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
        QuizDto dto = quizService.getQuizById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<QuizAttemptResultDto> startQuiz(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        User user = getAuthenticatedUser(principal);
        QuizAttemptResultDto dto = quizService.startQuiz(id, user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<QuizAttemptResultDto> submitQuiz(
            @PathVariable Long attemptId,
            @RequestBody QuizSubmissionDto submission,
            @AuthenticationPrincipal UserDetails principal) {
        User user = getAuthenticatedUser(principal);
        QuizAttemptResultDto dto = quizService.submitQuiz(attemptId, submission, user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/attempts/{attemptId}")
    public ResponseEntity<QuizAttemptResultDto> getAttemptResult(
            @PathVariable Long attemptId,
            @AuthenticationPrincipal UserDetails principal) {
        User user = getAuthenticatedUser(principal);
        QuizAttemptResultDto dto = quizService.getAttemptResult(attemptId, user);
        return ResponseEntity.ok(dto);
    }
}
