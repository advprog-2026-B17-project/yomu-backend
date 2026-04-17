package id.ac.ui.cs.advprog.yomubackend.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizCompletedEvent {
    private Long userId;
    private Long textId;
    private Long quizId;
    private Long attemptId;
    private Integer score;
    private LocalDateTime completedAt;
}