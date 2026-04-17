package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradingResultDto {
    private Long questionId;
    private Boolean isCorrect;
    private Integer score;
    private String feedback;
    private String correctAnswer;
    private String userAnswer; // jawaban yang diberikan user
}
