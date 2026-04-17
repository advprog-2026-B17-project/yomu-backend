package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptResultDto {
    private Long attemptId;
    private Integer score;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private List<GradingResultDto> gradingResults;
}
