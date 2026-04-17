package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSubmissionDto {
    private List<AnswerDto> answers;
}
