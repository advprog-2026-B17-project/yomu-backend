package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.service.AchievementService;
import id.ac.ui.cs.advprog.yomubackend.event.QuizCompletedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementEventControllerTest {
    @Mock
    private AchievementService achievementService;

    private AchievementEventController controller;

    @BeforeEach
    void setUp() {
        controller = new AchievementEventController(achievementService);
    }

    @Test
    void handleQuizCompleted_ReturnsProcessedStatus() {
        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .attemptId(100L)
                .userId(1L)
                .score(3)
                .build();
        when(achievementService.processQuizCompletedEvent(event)).thenReturn(true);

        ResponseEntity<Map<String, String>> response = controller.handleQuizCompleted(event);

        assertEquals("processed", response.getBody().get("status"));
        verify(achievementService).processQuizCompletedEvent(event);
    }

    @Test
    void handleQuizCompleted_ReturnsDuplicateStatus() {
        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .attemptId(100L)
                .userId(1L)
                .score(3)
                .build();
        when(achievementService.processQuizCompletedEvent(event)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = controller.handleQuizCompleted(event);

        assertEquals("duplicate", response.getBody().get("status"));
    }
}
