package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.service.AchievementService;
import id.ac.ui.cs.advprog.yomubackend.event.QuizCompletedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class AchievementEventController {
    private final AchievementService achievementService;

    public AchievementEventController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping("/quiz-completed")
    public ResponseEntity<Map<String, String>> handleQuizCompleted(
            @RequestBody QuizCompletedEvent event) {
        boolean processed = achievementService.processQuizCompletedEvent(event);
        return ResponseEntity.ok(Map.of("status", processed ? "processed" : "duplicate"));
    }
}
