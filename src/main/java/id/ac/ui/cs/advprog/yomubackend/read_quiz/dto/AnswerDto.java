package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {
    private Long questionId;
    private String userAnswer;
}
